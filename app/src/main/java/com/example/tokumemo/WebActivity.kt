package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import decrypt

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: WebViewModel
//    private lateinit var urlText: TextView

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

        findViewById<ImageButton>(R.id.back_button).setOnClickListener{
            webView.goBack()
        }

        findViewById<ImageButton>(R.id.forward_button).setOnClickListener {
            webView.goForward()
        }

        findViewById<ImageButton>(R.id.browser_button).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(urlString))
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.menu_button).setOnClickListener {
            Toast.makeText(this,
                "近日中に実装",
                Toast.LENGTH_SHORT
            ).show()
        }


        viewModel = ViewModelProvider(this)[WebViewModel::class.java]
        webViewSetup()

        Log.d("PRINT",getPassword(this) ?: "getPassword_nil")

    }
    // パスワードを復号化して取得する
    fun getPassword(context: Context): String? {
        // shaaredPreferencesから読み込み
        val encryptedPassword = getSharedPreferences("my_settings", Context.MODE_PRIVATE).getString("encrypted_password", null)
        // アンラップ
        encryptedPassword ?: return null
        // 復号化
        return decrypt("password", encryptedPassword)
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

                val uri = Uri.parse(url)
                val domain = uri.host
                findViewById<TextView>(R.id.urlText).text = domain?:""

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
                        // アンケート解答の催促画面
                        webView.evaluateJavascript("document.getElementById('ctl00_phContents_ucTopEnqCheck_link_lnk').click();", null)

                    }
                    WebViewModel.JavaScriptType.syllabus -> {
                        val subjectName = intent.getStringExtra("SYLLABUS_subject_name").toString()
                        val teacherName = intent.getStringExtra("SYLLABUS_teacher_name").toString()

                        // 検索中は、画面を消すことにより、ユーザーの別操作を防ぐ
//                        webView.visibility = INVISIBLE

                        // シラバスの検索画面
                        // ネイティブでの検索内容をWebに反映したのち、検索を行う
                        webView.evaluateJavascript("document.getElementById('ctl00_phContents_txt_sbj_Search').value='$subjectName'", null)
                        webView.evaluateJavascript("document.getElementById('ctl00_phContents_txt_staff_Search').value='$teacherName'", null)
                        webView.evaluateJavascript("document.getElementById('ctl00_phContents_ctl06_btnSearch').click();", null)
                        // フラグを下ろす
                        DataManager.canExecuteJavascript = false
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
//                                val dialog = RequireCorrectPasswordDialog()
//                                dialog.show(supportFragmentManager, "simple")
                            }
                        } else if (DataManager.jsCount == -1) {
//                            Toast.makeText(applicationContext, "トクメモ＋ゲストユーザーなのでパスワード自動入力を行いませんでした。", Toast.LENGTH_LONG).show()
                        }
                    }
                    WebViewModel.JavaScriptType.loginOutlook -> {

                    }
                    WebViewModel.JavaScriptType.loginCareerCenter -> {
                        // 徳島大学キャリアセンター室
                        // 自動入力を行う(cアカウントは同じ、パスワードは異なる可能性あり)
                        // ログインボタンは自動にしない(キャリアセンターと大学パスワードは人によるが同じではないから)
                        webView.evaluateJavaScript("document.getElementsByName('user_id')[0].value='\(dataManager.cAccount)'", completionHandler:  nil)
                        webView.evaluateJavaScript("document.getElementsByName('user_password')[0].value='\(dataManager.password)'", completionHandler:  nil)
                        // フラグを下ろす
                        dataManager.canExecuteJavascript = false
                    }
                    else -> {

                    }
                }
                // シラバス検索完了後のURLに変化していたらwebViewを表示
//                if (url.toString() in "http://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/SearchMain.aspx") {
//                    webView.visibility = GONE
//                }
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
            PREF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return prefs.getString(KEY, "")!! // nilの場合は空白を返す
    }
}