package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.manager.MainModel
import com.example.tokumemo.manager.DataManager
import com.example.tokumemo.manager.WebViewModel
import java.io.FileNotFoundException

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: WebViewModel

    private var urlString = ""
    private var isConnectToNetwork = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // 完了ボタン
        findViewById<Button>(R.id.finish_button).setOnClickListener{
            finish()
        }

        // reloadボタン
        findViewById<Button>(R.id.reload_button).setOnClickListener{
            webView.reload()
        }

        viewModel = ViewModelProvider(this)[WebViewModel::class.java]
        webViewSetup()

    }

    // MainActivityからデータを受け取ったデータを基にURLを読み込んでサイトを開く
    private fun webViewLoadUrl() {
        // MainActivityからURLを受け取る
        val receivedURL = intent.getStringExtra("PAGE_KEY").toString()
        webView.loadUrl(receivedURL)
    }


    @SuppressLint("SetJavaScriptEnabled") // JavaScriptを有効に
    private fun webViewSetup() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        // 読み込み時にページ横幅を画面幅に無理やり合わせる
//        webView.settings.loadWithOverviewMode = true
//        // ワイドビューポートへの対応
//        webView.settings.useWideViewPort = true
//        // 拡大縮小対応
//        webView.settings.builtInZoomControls = true

        // 検索アプリで開かない
        webView.webViewClient = object : WebViewClient(){

            // URLの読み込み開始
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                url ?: return // アンラップ

                urlString = url

                // タイムアウトの判定
                if (viewModel.isTimeout(urlString)) {
                    webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
                }
            }

            // 読み込み完了
            override fun onPageFinished(view: WebView?, url: String?) {
                urlString = url!! // fatalError

                when (viewModel.anyJavaScriptExecute(urlString)) {
                    WebViewModel.JavaScriptType.skipReminder -> {

                    }
                    WebViewModel.JavaScriptType.syllabus -> {

                    }

                    // ログイン画面に飛ばされた場合
                    WebViewModel.JavaScriptType.loginIAS -> {

                        if (shouldShowPasswordView()) {
                            // パスワード登録画面を表示
                            val intent = Intent(applicationContext, PasswordActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else if (DataManager.canExecuteJavascript && DataManager.jsCount >= 0) {
                            if (DataManager.jsCount < 2) {
                                Log.i("jsCount", DataManager.jsCount.toString())
                                DataManager.jsCount += 1
                                val cAccount = encryptedLoad("KEY_cAccount")
                                val password = encryptedLoad("KEY_password")

                                webView.evaluateJavascript(
                                    "document.getElementById('username').value= '$cAccount'",
                                    null
                                )
                                webView.evaluateJavascript(
                                    "document.getElementById('password').value= '$password'",
                                    null
                                )
                                webView.evaluateJavascript(
                                    "document.getElementsByClassName('form-element form-button')[0].click();",
                                    null
                                )
                                // フラグを下ろす
                                DataManager.canExecuteJavascript = false
                            } else {
                                val dialog = RequireCorrectPasswordDialog()
                                dialog.show(supportFragmentManager, "simple")
                            }
                        } else if (DataManager.jsCount == -1) {
                            Toast.makeText(applicationContext, "トクメモ＋ゲストユーザーなのでパスワード自動入力を行いませんでした。", Toast.LENGTH_LONG).show()
                        }
                    }
                    WebViewModel.JavaScriptType.loginOutlook -> {

                    }
                    WebViewModel.JavaScriptType.loginCareerCenter -> {

                    }
                    else -> {

                    }
                }
                super.onPageFinished(view, url)
            }
        }
        webViewLoadUrl()
    }

//    // 端末の戻るボタンが押されたとき
//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        // 戻るボタンが押される かつ webviewで前に戻ることができるとき
//        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//            // 前のページに戻る
//            webView.goBack()
//            return true
//        }
//
//        return super.onKeyDown(keyCode, event)
//    }

    // ハスワードを登録しているか判定し、パスワード画面の表示を行うべきか判定
    private fun shouldShowPasswordView():Boolean {
        val cAccount = encryptedLoad("KEY_cAccount")
        val password = encryptedLoad("KEY_password")
        return (cAccount == "" || password == "")
    }

    // 以下、暗号化してデバイスに保存する(PasswordActivityにも存在するので今後、統一)
    companion object {
        const val PREF_NAME = "encrypted_prefs"
    }
    // 読み込み
    private fun encryptedLoad(KEY: String): String {
        val mainKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            applicationContext,
            WebActivity.PREF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return prefs.getString(KEY, "")!! // nilの場合は空白を返す
    }
}