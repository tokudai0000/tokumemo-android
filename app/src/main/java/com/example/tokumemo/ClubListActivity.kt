package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tokumemo.manager.DataManager
import com.example.tokumemo.manager.MainModel

class ClubListActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel
    private var isConnectToNetwork = false

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.clublist)

        // ネット接続できているか
        // ConnectivityManagerの取得
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // NetworkCapabilitiesの取得
        // 引数にcm.activeNetworkを指定し、現在アクティブなデフォルトネットワークに対応するNetworkオブジェクトを渡している
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            isConnectToNetwork = true
        }

        if (isConnectToNetwork){
            webViewSetup()
        } else {
            val text = findViewById<TextView>(R.id.noNetWorkText)
            text.visibility = View.VISIBLE
        }

        val goTop = findViewById<Button>(R.id.goTop)
        goTop.setOnClickListener{
            val intent = Intent(this, ClubListActivity::class.java)
            startActivity(intent)
            finish()
        }

        // メニューバー
        val home = findViewById<Button>(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            DataManager.canExecuteJavascript = true
            finish()
        }

        val news = findViewById<Button>(R.id.news)
        news.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val clubList = findViewById<Button>(R.id.review)
        clubList.setOnClickListener{
            val intent = Intent(this, ClubListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val settings = findViewById<Button>(R.id.settings)
        settings.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // WebViewの設定
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
        webView = findViewById(R.id.clubListWebView)
        webView.settings.javaScriptEnabled = true
        viewModel = ViewModelProvider(this)[MainModel::class.java]

        // 検索アプリで開かない
        webView.webViewClient = object : WebViewClient(){}

        // 読み込み時にページ横幅を画面幅に無理やり合わせる
        webView.settings.loadWithOverviewMode = true
        // ワイドビューポートへの対応
        webView.settings.useWideViewPort = true
        // 拡大縮小対応
        webView.settings.builtInZoomControls = true

        webView.loadUrl("https://tokudai0000.github.io/club-list/")
    }

    // 端末の戻るボタンが押されたとき
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // 戻るボタンが押される かつ webviewで前に戻ることができるとき
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            // 前のページに戻る
            webView.goBack()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }
}