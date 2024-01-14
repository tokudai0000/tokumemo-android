package com.tokudai0000.tokumemo.ui.splash

import UnivAuthRepository
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.common.UrlCheckers
import com.tokudai0000.tokumemo.data.DataManager
import com.tokudai0000.tokumemo.data.DataManager.Companion.canExecuteJavascript
import com.tokudai0000.tokumemo.ui.RootActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {
    companion object {

        const val EXTRA_RESULT = "result"

        fun createIntent(context: Context) =
            Intent(context, SplashActivity::class.java)
    }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer("Schedule", false).schedule(10000) {
            val returnIntent = Intent()
            returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        GlobalScope.launch {
            try {
                val termVersion = getCurrentTermVersion()
                DataManager.agreementVer = termVersion

                // UIスレッドで実行
                withContext(Dispatchers.Main) {
                    val KEY = "KEY_agreementVersion"
                    val sharedPreferences = getSharedPreferences("my_settings", Context.MODE_PRIVATE)
                    val oldAgreementVer = sharedPreferences.getString(KEY, null).toString()

                    if (termVersion == oldAgreementVer) {
                        webView.loadUrl(Url.UniversityTransitionLogin.urlString)
                    }else{
                        val returnIntent = Intent()
                        returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "AgreementActivity")
                        setResult(Activity.RESULT_OK, returnIntent)
                        finish()
                    }
                }
                println("Current term version: $termVersion")
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
            }
        }

        configureWebView()
        configureLoginStatus()
        configureCopylight()
    }

    private fun configureWebView() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        // 非表示にする
        webView.visibility = View.GONE

        webView.webViewClient = object : WebViewClient() {

            // Url.UniversityTransitionLoginは強制で外部検索エンジンが開かれるので、ここで禁止している
            // 外部ブラウザで開かずにWebView内で処理する
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return true
            }


            // URLの読み込み開始
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                // アンラップ
                url ?: return

                // タイムアウト判定
                if (UrlCheckers.isUniversityServiceTimeoutURL(url)) {
                    webView.loadUrl(Url.UniversityTransitionLogin.urlString)
                }

                // ログイン成功
                if (UrlCheckers.isImmediatelyAfterLoginURL(url)) {
                    val returnIntent = Intent()
                    returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }

                // ログイン失敗
                if (UrlCheckers.isFailureUniversityServiceLoggedInURL(url)) {
                    val returnIntent = Intent()
                    returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }

            // 読み込み完了
            override fun onPageFinished(view: WebView?, url: String?) {
                // アンラップ
                url ?: return

                // ログイン処理を行うURLか判定
                if (UrlCheckers.shouldInjectJavaScript(url, canExecuteJavascript, urlType = UrlCheckers.UrlType.UniversityLogin)) {
                    canExecuteJavascript = false
                    val univAuth = UnivAuthRepository(this@SplashActivity).fetchUnivAuth()
                    val cAccount = univAuth.accountCID //"c612333035x" //getPassword(view!!.context,"KEY_cAccount")
                    val password = univAuth.password //getPassword(view!!.context,"KEY_password")
                    webView.evaluateJavascript("document.getElementById('username').value= '$cAccount'", null)
                    webView.evaluateJavascript("document.getElementById('password').value= '$password'", null)
                    webView.evaluateJavascript("document.getElementsByClassName('form-element form-button')[0].click();", null)
                }

                super.onPageFinished(view, url)
            }
        }
    }

    private fun configureLoginStatus() {
        val loginStatus: TextView = findViewById(R.id.loginStatusTextView)
        loginStatus.text = "認証確認中"
        loginStatus.gravity = Gravity.CENTER
        loginStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }

    private fun configureCopylight() {
        val copylight: TextView = findViewById(R.id.copylightTextView)
        copylight.text = "Developed by Tokushima Univ students \n GitHub @tokudai0000"
        copylight.gravity = Gravity.CENTER
        copylight.setTextColor(Color.LTGRAY)
        copylight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }

    private fun isTermsVersionDifferent(current: String, accepted: String): Boolean {
        return current != accepted
    }

    // 関数をsuspend関数に変更
    private suspend fun getCurrentTermVersion(): String {
        val apiUrl = "https://tokudai0000.github.io/tokumemo_resource/api/v1/current_term_version.json"

        return withContext(Dispatchers.IO) {
            try {
                val responseData = URL(apiUrl).readText()
                val jsonData = JSONObject(responseData)

                // Jsonデータから内容物を取得
                jsonData.getString("currentTermVersion")
            } catch (e: Exception) {
                // エラー処理、適宜エラーメッセージを返す
                "Error: ${e.message}"
            }
        }
    }
}