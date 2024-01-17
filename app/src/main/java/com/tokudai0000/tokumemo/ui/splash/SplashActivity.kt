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
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.common.UrlCheckers
import com.tokudai0000.tokumemo.data.repository.AcceptedTermVersionRepository
import com.tokudai0000.tokumemo.ui.RootActivity
import com.tokudai0000.tokumemo.ui.agreement.AgreementActivity
import java.util.Timer
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity(R.layout.activity_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    companion object {

        private var canExecuteJavascript = true

        fun createIntent(context: Context) = Intent(context, SplashActivity::class.java)
    }

    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureWebView()
        configureLoginStatus()
        configureCopyright()
        configureTermText()
        configureTimer()
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
                if (UrlCheckers.shouldInjectJavaScript(url,
                        canExecuteJavascript,
                        urlType = UrlCheckers.UrlType.UniversityLogin)) {

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

    private fun configureCopyright() {
        val copyright: TextView = findViewById(R.id.copylightTextView)
        copyright.text = "Developed by Tokushima Univ students \n GitHub @tokudai0000"
        copyright.gravity = Gravity.CENTER
        copyright.setTextColor(Color.LTGRAY)
        copyright.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }

    private fun configureTermText() {
        viewModel.currentTermVersion.observe(this) { currentTermVersion ->
            // 既に同意済みの規約バージョンを取得
            val oldAgreementVer = AcceptedTermVersionRepository(this).fetchAcceptedTermVersion()
            AKLog(AKLogLevel.DEBUG, "termVersionAPI取得: $currentTermVersion、同意済み: $oldAgreementVer")

            // APIから取得した最新の規約バージョンと、デバイスに保存された既に同意したバージョンを比較
            if (currentTermVersion == oldAgreementVer) {
                // 同意済みであれば、ログイン処理
                webView.loadUrl(Url.UniversityTransitionLogin.urlString)
            }else{
                // 規約同意画面を表示させる
                val returnIntent = Intent()
                AgreementActivity.currentTermVersion = currentTermVersion
                returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "AgreementActivity")
                setResult(Activity.RESULT_OK, returnIntent)
                finish()
            }
        }
        viewModel.getCurrentTermVersion()
    }

    private fun configureTimer() {
        // 例外発生処理(10 s後に、Main画面へ遷移する)
        Timer("Schedule", false).schedule(10000) {
            val returnIntent = Intent()
            returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}