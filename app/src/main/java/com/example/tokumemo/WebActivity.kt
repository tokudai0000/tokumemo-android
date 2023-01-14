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

class WebActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel

    private var urlString = ""
    private var isConnectToNetwork = false

//    class ViewHolderList (item: View) : RecyclerView.ViewHolder(item) {
//        val characterList: TextView = item.findViewById(R.id.menuTitle)
//    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)

        // ネット接続できているか
        // ConnectivityManagerの取得
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // NetworkCapabilitiesの取得
        // 引数にcm.activeNetworkを指定し、現在アクティブなデフォルトネットワークに対応するNetworkオブジェクトを渡している
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            isConnectToNetwork = true
        }

        // テストユーザーの場合はjsCountを-1にしておく
//        if (encryptedLoad("KEY_studentNumber") == "0123456789" && encryptedLoad("KEY_password") == "0000"){
//            DataManager.jsCount = -1
//            Log.i("jsCount", DataManager.jsCount.toString())
//        } else {
//            DataManager.jsCount = 0
//        }
        DataManager.jsCount = 0

        // 配列の生成
        val menuArray = arrayOf("ホーム", " - 教務事務システム", " - manaba", " - メール", " - 時間割", "News", "Settings")
        val listView = findViewById<ListView>(R.id.menuList)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuArray)
        listView.adapter = adapter

        val menuButton = findViewById<ImageButton>(R.id.menuButton)
        menuButton.setOnClickListener{
            if (listView.visibility == View.GONE){
                listView.visibility = View.VISIBLE
            }else{
                listView.visibility = View.GONE
            }
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            when (position) {
                0 -> {//ホーム
                    listView.visibility = View.GONE
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    DataManager.canExecuteJavascript = true
                    finish()
                }
                1 -> {//教務システム
                    listView.visibility = View.GONE
                    webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx")
                }
                2 -> {//manaba
                    listView.visibility = View.GONE
                    webView.loadUrl("https://manaba.lms.tokushima-u.ac.jp/ct/home")
                }
                3 -> {//メール
                    listView.visibility = View.GONE
                    webView.loadUrl("https://outlook.office365.com/mail/")
                }
                4 -> {//時間割
                    listView.visibility = View.GONE
                    webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx")
                }
                5 -> {//ニュース
                    listView.visibility = View.GONE
                    val intent = Intent(this, NewsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                6 -> {//Settings
                    listView.visibility = View.GONE
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }

        val back = findViewById<Button>(R.id.back)
        back.setOnClickListener{
            finish()
        }

        if (isConnectToNetwork){
            webViewSetup()
        } else {
            val text = findViewById<TextView>(R.id.text)
            text.visibility = View.VISIBLE
        }
    }

    // MainActivityからデータを受け取ったデータを基にURLを読み込んでサイトを開く
    private fun webViewLoadUrl() {
        // MainActivityからURLを受け取る
        val receivedURL = intent.getStringExtra("PAGE_KEY").toString()
        webView.loadUrl(receivedURL)
    }

    // WebViewの設定
    @SuppressLint("SetJavaScriptEnabled")
    private fun webViewSetup() {
        webView = findViewById(R.id.webView)
        webView.settings.javaScriptEnabled = true
        viewModel = ViewModelProvider(this)[MainModel::class.java]

        // 検索アプリで開かない
        webView.webViewClient = object : WebViewClient(){
            // URLの読み込みが始まった時の処理
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                if (url != null) {
                    urlString = url
                }
                // タイムアウトをしていた場合
                // 下のonPageFinishedでも実装しているが、タイムアウト検知はできるだけ早い方がいいということでここにも実装
                if (viewModel.isTimeout(urlString)) {
                    // ログイン処理を始める
                    webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
                }
            }

            // URLの読み込みが終わった時の処理
            override fun onPageFinished(view: WebView?, url: String?) {
                if (url != null) {
                    urlString = url
                }

                // タイムアウトをしていた場合
                if (viewModel.isTimeout(urlString)) {
                    // ログイン処理を始める
                    webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
                }

                when (viewModel.anyJavaScriptExecute(urlString)) {
                    // ログイン画面に飛ばされた場合
                    MainModel.JavaScriptType.loginIAS -> {

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
                    else -> {}
                }
                super.onPageFinished(view, url)
            }
        }

        // 読み込み時にページ横幅を画面幅に無理やり合わせる
        webView.settings.loadWithOverviewMode = true
        // ワイドビューポートへの対応
        webView.settings.useWideViewPort = true
        // 拡大縮小対応
        webView.settings.builtInZoomControls = true

        webViewLoadUrl()
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