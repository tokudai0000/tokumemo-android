package com.example.tokumemo

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.flag.MainModel
import com.example.tokumemo.manager.DataManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class OthersActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)

        val settingsScreen = findViewById<LinearLayout>(R.id.settings_screen)
        val title = findViewById<TextView>(R.id.settings_title)
        val bar = findViewById<ConstraintLayout>(R.id.backBar)
        val back = findViewById<Button>(R.id.backButton2)

        // メニューバー
        val Home = findViewById<Button>(R.id.home)
        Home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val News = findViewById<Button>(R.id.news)
        News.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val Others = findViewById<Button>(R.id.others)
        Others.setOnClickListener{
            val intent = Intent(this, OthersActivity::class.java)
            startActivity(intent)
            finish()
        }

        // パスワード設定を押したとき
        val passwordSetting = findViewById<Button>(R.id.passwordSetting)
        passwordSetting.setOnClickListener{
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }

        // 公式SNSを押したとき
        val sns = findViewById<Button>(R.id.sns)
        sns.setOnClickListener{
            openWeb("https://twitter.com/tokumemo0000")
        }

        // ホームページを押したとき
        val homePage = findViewById<Button>(R.id.homePage)
        homePage.setOnClickListener{
            openWeb("https://lit.link/developers")
        }

        // 利用規約を押したとき
        val termsOfService = findViewById<Button>(R.id.termsOfService)
        termsOfService.setOnClickListener{
            openWeb("https://github.com/tokudai0000/document/blob/main/tokumemo/terms/TermsOfService.txt")
        }

        // プライバシーポリシーを押したとき
        val privacyPolicy = findViewById<Button>(R.id.privacyPolicy)
        privacyPolicy.setOnClickListener{
            openWeb("https://github.com/tokudai0000/document/blob/main/tokumemo/terms/PrivacyPolicy.txt")
        }

        // 戻るボタンを押したとき
        back.setOnClickListener{
            settingsScreen.visibility = View.VISIBLE
            title.visibility = View.VISIBLE
            webView.visibility = View.GONE
            bar.visibility = View.INVISIBLE
        }
    }

    private fun openWeb(url: String) {

        val settingsScreen = findViewById<LinearLayout>(R.id.settings_screen)
        val title = findViewById<TextView>(R.id.settings_title)
        val bar = findViewById<ConstraintLayout>(R.id.backBar)

        settingsScreen.visibility = View.INVISIBLE
        title.visibility = View.INVISIBLE
        bar.visibility = View.VISIBLE


        webView = findViewById(R.id.web)
        webView.settings.javaScriptEnabled = true
        viewModel = ViewModelProvider(this).get(MainModel::class.java)

        // 検索アプリで開かない
        webView.webViewClient = object : WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                // webビューを表示モードに
                webView.visibility = View.VISIBLE
            }
        }
        // 読み込み時にページ横幅を画面幅に無理やり合わせる
        webView.getSettings().setLoadWithOverviewMode( true );
        // ワイドビューポートへの対応
        webView.getSettings().setUseWideViewPort( true );
        // 拡大縮小対応
        webView.getSettings().setBuiltInZoomControls(true);

        webView.loadUrl(url)
    }
}