package com.example.tokumemo

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.R
import com.example.tokumemo.flag.MainModel
import com.example.tokumemo.manager.DataManager

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel

    private var urlString = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        viewModel = ViewModelProvider(this).get(MainModel::class.java)

        // 検索アプリで開かない
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) {
                    urlString = url
                }

                when (viewModel.anyJavaScriptExecute(urlString)) {
                    MainModel.JavaScriptType.loginIAS -> {

                        if (shouldShowPasswordView()) {
                            // パスワード登録画面を表示
                            val intent = Intent(applicationContext, PasswordActivity::class.java)
                            startActivity(intent)
                            // 戻ってきた時、startForPasswordActivityを呼び出す
//                          startForPasswordActivity.launch(intent)
                        }
                        else {
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
                        }
                    }
                    else -> {}
                }

                super.onPageFinished(view, url)
            }
        }

        // MainActivityからデータを受け取る
        // どのWebサイトを開こうとしているかをIdで判別
        var receivedData = intent.getStringExtra("PAGE_KEY")
        var pageId = 99
        // 整数型に変換
        if (receivedData != null) {
            pageId = receivedData.toInt()
        }

        webView.loadUrl(viewModel.isAnyWebsite(pageId))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.web_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.setPassword -> {
                val intent = Intent(applicationContext, PasswordActivity::class.java)
                startActivity(intent)
            }
            R.id.login -> {
                when (viewModel.anyJavaScriptExecute(urlString)) {
                    MainModel.JavaScriptType.loginIAS -> {

                        if (shouldShowPasswordView()) {
                            // パスワード登録画面を表示
                            val intent = Intent(applicationContext, PasswordActivity::class.java)
                            startActivity(intent)
                            // 戻ってきた時、startForPasswordActivityを呼び出す
//                          startForPasswordActivity.launch(intent)
                        }
                        else {
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
                        }
                    }
                    else -> {}
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // PasswordActivityで登録ボタンを押した場合、再度ログイン処理を行う
//    private val startForPasswordActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            // ログイン処理を行う
//            webView.loadUrl("https://my.ait.tokushima-u.ac.jp/portal/")
//        }
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
    // 保存
    private fun encryptedSave(KEY: String, text: String) {
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
        with (prefs.edit()) {
            putString(KEY, text)
            apply()
        }
    }
}