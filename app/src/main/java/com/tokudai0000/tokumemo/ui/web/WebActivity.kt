package com.tokudai0000.tokumemo.ui.web

import UnivAuthRepository
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.common.UrlCheckers
import com.tokudai0000.tokumemo.ui.RootActivity
import com.tokudai0000.tokumemo.ui.agreement.AgreementActivity

class WebActivity : AppCompatActivity(R.layout.activity_web) {

    // 少量なのでViewModelに書かなくていいかな
    private var urlString = ""
    private var canExecuteJavascript = true

    private lateinit var webView: WebView

    companion object {
        val KEY_URL: String = "KEY_URL"

        fun createIntent(context: Context) = Intent(context, AgreementActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureWebView()

        webViewLoadUrl()

        // 完了ボタン
        findViewById<Button>(R.id.finish_button).setOnClickListener{
            finish()
        }

        // reloadボタン
        findViewById<Button>(R.id.reload_button).setOnClickListener{
            webView.reload()
            AKLog(AKLogLevel.DEBUG, "reloadボタンが押された")
        }

        findViewById<ImageButton>(R.id.forward_button).setOnClickListener {
            webView.goForward()
            AKLog(AKLogLevel.DEBUG, "goForwardボタンが押された")
        }

        findViewById<ImageButton>(R.id.back_button).setOnClickListener{
            webView.goBack()
            AKLog(AKLogLevel.DEBUG, "goBackボタンが押された")
        }

        findViewById<ImageButton>(R.id.browser_button).setOnClickListener {
            AKLog(AKLogLevel.DEBUG, "ブラウザ表示 - $urlString")
            val url = Uri.parse(urlString)
            val intent = Intent(Intent.ACTION_VIEW, url)
            startActivity(intent)
        }
    }

    // MainActivityからデータを受け取ったデータを基にURLを読み込んでサイトを開く
    private fun webViewLoadUrl() {
        // MainActivityからURLを受け取る
        val receivedURL = intent.getStringExtra(KEY_URL).toString()
        webView.loadUrl(receivedURL)
    }

    private fun configureWebView() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        // 読み込み時にページ横幅を画面幅に無理やり合わせる
//        webView.settings.loadWithOverviewMode = true
//        // ワイドビューポートへの対応
//        webView.settings.useWideViewPort = true
//        // 拡大縮小対応
//        webView.settings.builtInZoomControls = true
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

                // ブラウザで開く用として、URLを記録しておく
                urlString = url

                // ドメイン名を表示
                val domain = Uri.parse(url).host
                findViewById<TextView>(R.id.urlText).text = domain?:""

                // タイムアウト判定
                if (UrlCheckers.isUniversityServiceTimeoutURL(url)) {
                    webView.loadUrl(Url.UniversityTransitionLogin.urlString)
                }

                // pdfのリンクであれば
                UrlCheckers.convertToGoogleDocsViewerUrlIfNeeded(url)?.let {
                    // Google Docs Viewerを使用するURLに変換して再読み込み
                    webView.loadUrl(it)
                    AKLog(AKLogLevel.DEBUG, "PDFリンク： $it")
                }
            }

            // 読み込み完了
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // アンラップ
                url ?: return

                // ログイン処理を行うURLか判定
                if (UrlCheckers.shouldInjectJavaScript(url,
                        canExecuteJavascript,
                        urlType = UrlCheckers.UrlType.UniversityLogin)) {

                    canExecuteJavascript = false
                    val univAuth = UnivAuthRepository(this@WebActivity).fetchUnivAuth()
                    val cAccount = univAuth.accountCID
                    val password = univAuth.password
                    webView.evaluateJavascript("document.getElementById('username').value= '$cAccount'", null)
                    webView.evaluateJavascript("document.getElementById('password').value= '$password'", null)
                    webView.evaluateJavascript("document.getElementsByClassName('form-element form-button')[0].click();", null)
                }

                // アンケート解答の催促画面
                if (UrlCheckers.isSkipReminderURL(url)) {
                    canExecuteJavascript = false
                    webView.evaluateJavascript("document.getElementById('ctl00_phContents_ucTopEnqCheck_link_lnk').click();", null)
                }

                if (UrlCheckers.shouldInjectJavaScript(url,
                        canExecuteJavascript,
                        UrlCheckers.UrlType.OutlookLoginForm)) {

                    canExecuteJavascript = false
                    val univAuth = UnivAuthRepository(this@WebActivity).fetchUnivAuth()
                    val cAccount = univAuth.accountCID
                    val password = univAuth.password
                    webView.evaluateJavascript("document.getElementsByName('user_id')[0].value='$cAccount'", null)
                    webView.evaluateJavascript("document.getElementsByName('user_password')[0].value='$password'", null)
                }

                if (UrlCheckers.shouldInjectJavaScript(url,
                        canExecuteJavascript,
                        UrlCheckers.UrlType.TokudaiCareerCenter)) {

                    canExecuteJavascript = false
                    val univAuth = UnivAuthRepository(this@WebActivity).fetchUnivAuth()
                    webView.evaluateJavascript("document.getElementsByName('user_id')[0].value='$univAuth.cAccount'", null)
                    webView.evaluateJavascript("document.getElementsByName('user_password')[0].value='$univAuth.password'", null)
                }
            }
        }
    }
}