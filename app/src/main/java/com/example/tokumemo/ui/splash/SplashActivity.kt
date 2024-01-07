package com.example.tokumemo.ui.splash

import UnivAuthRepository
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tokumemo.R
import com.example.tokumemo.common.Url
import com.example.tokumemo.common.UrlCheckers
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.data.DataManager.Companion.canExecuteJavascript
import com.example.tokumemo.ui.MainActivity
import com.example.tokumemo.ui.agreement.AgreementActivity
import com.example.tokumemo.ui.pr.PublicRelationsActivity
import com.example.tokumemo.ui.web.WebActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        configureWebView()
        configureLoginStatus()
        configureCopylight()
    }

    private fun configureWebView() {
        val webView: WebView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(Url.UniversityTransitionLogin.urlString)

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
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                }

                // ログイン失敗
                if (UrlCheckers.isFailureUniversityServiceLoggedInURL(url)) {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
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
}