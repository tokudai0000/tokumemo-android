package com.example.tokumemo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.databinding.ActivityMainBinding
import com.example.tokumemo.flag.MainModel
import com.example.tokumemo.manager.DataManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.newFixedThreadPoolContext

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel
    private var urlString = ""

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {

            }
            R.id.passwordActivity -> {
                val intent = Intent(applicationContext, PasswordActivity::class.java)
                startActivity(intent)
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 隠れWebビューここから（ここで先にログイン処理のみしておく）
        webView = findViewById(R.id.loginView)
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

        webView.loadUrl("https://my.ait.tokushima-u.ac.jp/portal/")
        // 隠れWebビューここまで


        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        // 教務システムを押したとき
        val nextButton = findViewById<Button>(R.id.academicAffairsSystem)
        nextButton.setOnClickListener{
            val intent = Intent(this, WebActivity::class.java)
            val pageId = "2"
            // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
            intent.putExtra("PAGE_KEY",pageId)
            startActivity(intent)
        }


    }

//    オプションメニューバーは消す予定
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.goPage -> {
                val intent = Intent(applicationContext, WebActivity::class.java)
                startActivity(intent)
            }
            R.id.setPassword -> {
                val intent = Intent(applicationContext, PasswordActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // パスワードを登録しているか判定し、パスワード画面の表示を行うべきか判定
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

