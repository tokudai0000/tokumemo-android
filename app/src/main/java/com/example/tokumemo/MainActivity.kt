package com.example.tokumemo

import android.content.Context
import decrypt
import encrypt
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainModel::class.java]

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottomNavigation, navController)

        savePassword(this, "c611821006")
        Log.d("PRINT",getPassword(this) ?: "getPassword_nil")

        // 自動ログイン用WebView
//        val webView: WebView = findViewById(R.id.webView_for_login)
//        webView.settings.javaScriptEnabled = true
//        webView.webViewClient = object : WebViewClient(){
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                var urlString = ""
//                url?.let { urlString = it }
//
//                val type = viewModel.anyJavaScriptExecute(urlString)
//
//                // 徳島大学ログイン画面の場合
//                if (type == MainModel.JavaScriptType.loginIAS) {
//
//                    // エラーハンドリング
//                    if (!DataManager.canExecuteJavascript) {
//                        return
//                    }
//
//                    val cAccount = encryptedLoad("KEY_cAccount")
//                    val password = encryptedLoad("KEY_password")
//
//                    webView.evaluateJavascript(
//                        "document.getElementById('username').value= '$cAccount'",
//                        null
//                    )
//                    webView.evaluateJavascript(
//                        "document.getElementById('password').value= '$password'",
//                        null
//                    )
//                    webView.evaluateJavascript(
//                        "document.getElementsByClassName('form-element form-button')[0].click();",
//                        null
//                    )
//
//
//                }
//
//                when (viewModel.anyJavaScriptExecute(urlString)) {
//                    MainModel.JavaScriptType.loginIAS -> {
//                        if (DataManager.canExecuteJavascript && DataManager.jsCount >= 0) {
//                            if (DataManager.jsCount < 2) {
//                                Log.i("jsCount", DataManager.jsCount.toString())
//                                DataManager.jsCount += 1
//                                val cAccount = encryptedLoad("KEY_cAccount")
//                                val password = encryptedLoad("KEY_password")
//
//                                webView.evaluateJavascript(
//                                    "document.getElementById('username').value= '$cAccount'",
//                                    null
//                                )
//                                webView.evaluateJavascript(
//                                    "document.getElementById('password').value= '$password'",
//                                    null
//                                )
//                                webView.evaluateJavascript(
//                                    "document.getElementsByClassName('form-element form-button')[0].click();",
//                                    null
//                                )
//                            }
//                        }
//                    }
//                    else -> {}
//                }
//
//                super.onPageFinished(view, url)
//
//                // ついでに天気情報を更新しておく
//                binding.weatherText.text = resultText
//                iconUrl = "https://openweathermap.org/img/wn/" + encryptedLoad("icon") + ".png"
//                weatherWebView.loadUrl(iconUrl)
//            }
//        }
//        webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
//        // 隠れWebビューここまで

    }

    // パスワードを暗号化して保存する
    fun savePassword(context: Context, plainPassword: String) {
        // 暗号化
        val encryptedPassword = encrypt(context, "password", plainPassword)

        // shaaredPreferencesに保存
        var editor = getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putString("encrypted_password", encryptedPassword).commit()
        }
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
}


//    val PROVIDER = "AndroidKeyStore"
//    val ALGORITHM = "RSA"
//    val CIPHER_TRANSFORMATION = "RSA/ECB/PKCS1Padding"
//
//    /**
//     * テキストを暗号化する
//     * @param context
//     * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
//     * @param plainText 暗号化したいテキスト
//     * @return 暗号化されBase64でラップされた文字列
//     */
//    fun encrypt(context: Context, alias: String, plainText: String): String {
//        val keyStore = KeyStore.getInstance(PROVIDER)
//        keyStore.load(null)
//
//        // キーペアがない場合生成
//        if (!keyStore.containsAlias(alias)) {
//            val keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER)
//            keyPairGenerator.initialize(createKeyPairGeneratorSpec(context, alias))
//            keyPairGenerator.generateKeyPair()
//        }
//        val publicKey = keyStore.getCertificate(alias).getPublicKey()
//        val privateKey = keyStore.getKey(alias, null)
//
//        // 公開鍵で暗号化
//        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey)
//        val bytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
//
//        // SharedPreferencesに保存しやすいようにBase64でString化
//        return Base64.encodeToString(bytes, Base64.DEFAULT)
//    }
//
//    /**
//     * 暗号化されたテキストを復号化する
//     * @param alias キーペアを識別するためのエリアス。用途ごとに一意にする。
//     * @param encryptedText encryptで暗号化されたテキスト
//     * @return 復号化された文字列
//     */
//    fun decrypt(alias: String, encryptedText: String): String? {
//        val keyStore = KeyStore.getInstance(PROVIDER)
//        keyStore.load(null)
//        if (!keyStore.containsAlias(alias)) {
//            return null
//        }
//
//        // 秘密鍵で復号化
//        val privateKey = keyStore.getKey(alias, null)
//        val cipher = Cipher.getInstance(CIPHER_TRANSFORMATION)
//        cipher.init(Cipher.DECRYPT_MODE, privateKey)
//        val bytes = Base64.decode(encryptedText, Base64.DEFAULT)
//
//        val b = cipher.doFinal(bytes)
//        return String(b)
//    }
//
//    /**
//     * キーペアを生成する
//     */
//    fun createKeyPairGeneratorSpec(context: Context, alias: String): KeyPairGeneratorSpec {
//        val start = Calendar.getInstance()
//        val end = Calendar.getInstance()
//        end.add(Calendar.YEAR, 100)
//
//        return KeyPairGeneratorSpec.Builder(context)
//            .setAlias(alias)
//            .setSubject(X500Principal(String.format("CN=%s", alias)))
//            .setSerialNumber(BigInteger.valueOf(1000000))
//            .setStartDate(start.getTime())
//            .setEndDate(end.getTime())
//            .build()
//    }
//}

//    @RequiresApi(Build.VERSION_CODES.N)
//    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled", "SimpleDateFormat")
//    override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//
//    setContentView(R.layout.activity_main)
//
//    val bottom_navigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
//    val navController = findNavController(R.id.nav_host_fragment)
//    setupWithNavController(bottom_navigation, navController)

//        val currentYearSdf = SimpleDateFormat("yyyy")
//        val currentYear = currentYearSdf.format(Date())
//        val currentMonthSdf = SimpleDateFormat("MM")
//        val currentMonth = currentMonthSdf.format(Date())
//
//        // ネット接続できているか
//        // ConnectivityManagerの取得
//        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        // NetworkCapabilitiesの取得
//        // 引数にcm.activeNetworkを指定し、現在アクティブなデフォルトネットワークに対応するNetworkオブジェクトを渡している
//        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
//        if (capabilities != null) {
//            isConnectToNetwork = true
//        }
//
//        // 初回起動時に利用規約ダイアログを表示
//        if (encryptedLoad("isFirstTime") != "false") {
//            val dialog = FirstDialogFragment()
//            dialog.show(supportFragmentManager, "simple")
//            encryptedSave("isFirstTime", "false")
//        }
//        if (isConnectToNetwork){
//            // 天気情報表示準備(実際に表示するのは隠れWebビューのonPageFinishedの最後あたり)
//
//            binding = ActivityMainBinding.inflate(layoutInflater)
//            setContentView(binding.root)
//            // 天気を取得
//            getWeatherNews()
//            initWeatherWebIcon()
//
//            // 広告画像貼り付け
//            if (encryptedLoad("imageNum") != ""){
//                imageNum = encryptedLoad("imageNum").toInt()
//            }
//            val imageButton = findViewById<ImageButton>(R.id.image)
//
//            getAd()
//
//            imageButton.setOnClickListener{
//                if (adExistence){
//                    goWeb(adURL)
//                }
//            }
//
//            // 隠れWebビューここから（ここで先にログイン処理のみしておく）
//            webView = findViewById(R.id.loginView)
//            webView.settings.javaScriptEnabled = true
//            viewModel = ViewModelProvider(this)[MainModel::class.java]
//
//            DataManager.jsCount = 0
//
//            // 検索アプリで開かない
//            webView.webViewClient = object : WebViewClient(){
//                override fun onPageFinished(view: WebView?, url: String?) {
//                    if (url != null) {
//                        urlString = url
//                    }
//
//                    when (viewModel.anyJavaScriptExecute(urlString)) {
//                        MainModel.JavaScriptType.loginIAS -> {
//
//                            if (DataManager.canExecuteJavascript && DataManager.jsCount >= 0) {
//                                if (DataManager.jsCount < 2) {
//                                    Log.i("jsCount", DataManager.jsCount.toString())
//                                    DataManager.jsCount += 1
//                                    val cAccount = encryptedLoad("KEY_cAccount")
//                                    val password = encryptedLoad("KEY_password")
//
//                                    webView.evaluateJavascript(
//                                        "document.getElementById('username').value= '$cAccount'",
//                                        null
//                                    )
//                                    webView.evaluateJavascript(
//                                        "document.getElementById('password').value= '$password'",
//                                        null
//                                    )
//                                    webView.evaluateJavascript(
//                                        "document.getElementsByClassName('form-element form-button')[0].click();",
//                                        null
//                                    )
//                                }
//                            }
//                        }
//                        else -> {}
//                    }
//
//                    super.onPageFinished(view, url)
//
//                    // ついでに天気情報を更新しておく
//                    binding.weatherText.text = resultText
//                    iconUrl = "https://openweathermap.org/img/wn/" + encryptedLoad("icon") + ".png"
//                    weatherWebView.loadUrl(iconUrl)
//                }
//            }
//            webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
//            // 隠れWebビューここまで
//        }
//
//        // 学生証バーコード生成
//        createStudentCard()
//
//        // メニューバー
//        val home = findViewById<Button>(R.id.home)
//        home.setOnClickListener{
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            DataManager.canExecuteJavascript = true
//            finish()
//        }
//
//        val news = findViewById<Button>(R.id.news)
//        news.setOnClickListener{
//            val intent = Intent(this, NewsActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        val clubList = findViewById<Button>(R.id.review)
//        clubList.setOnClickListener{
//            val intent = Intent(this, ClubListActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        val settings = findViewById<Button>(R.id.settings)
//        settings.setOnClickListener{
//            val intent = Intent(this, SettingsActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//
//        // 天気を押したとき
//        val weather = findViewById<Button>(R.id.weatherButton)
//        val weatherIcon = findViewById<Button>(R.id.weatherButton2)
//        weather.setOnClickListener{
//            goWeb("https://www.jma.go.jp/bosai/forecast/#area_type=class20s&area_code=3620100")
//        }
//        weatherIcon.setOnClickListener{
//            goWeb("https://www.jma.go.jp/bosai/forecast/#area_type=class20s&area_code=3620100")
//        }
//

//        // 図書館カレンダーを押したとき
//        val libraryCarender = findViewById<Button>(R.id.libraryCarender)
//        libraryCarender.setOnClickListener{
//            val term = if (currentMonth.toInt() <= 3){
//                (currentYear.toInt()-1).toString()
//            }else{
//                currentYear
//            }
//            // ダイアログの表示
//            val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this, R.style.FirstDialogStyle)
//            alertDialog.setTitle("図書館の所在地")
//            alertDialog.setMessage("メッセージ")
//            alertDialog.setPositiveButton("常三島",
//                DialogInterface.OnClickListener { dialog, whichButton ->
//                    val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_$term.pdf&embedded=true"
//                    goWeb(libraryURL)
//                })
//            alertDialog.setNegativeButton("蔵本",
//                DialogInterface.OnClickListener { dialog, whichButton ->
//                    val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_$term.pdf&embedded=true"
//                    goWeb(libraryURL)
//                })
//            alertDialog.setOnCancelListener(DialogInterface.OnCancelListener {
//                // キャンセルの処理
//            })
//            alertDialog.show()
//        }

//        // 今学期の成績を押したとき
//        val Button6 = findViewById<Button>(R.id.result)
//        Button6.setOnClickListener{
//            var resultURL = ""
//            resultURL = if (currentMonth.toInt() <= 3){
//                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year=" + (currentYear.toInt()-1).toString()
//            }else{
//                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year=$currentYear"
//            }
//            goWeb(resultURL)
//        }
//    }
//
//    // パスワードを登録しているか判定し、パスワード画面の表示を行うべきか判定
//    private fun shouldShowPasswordView():Boolean {
//        val cAccount = encryptedLoad("KEY_cAccount")
//        val password = encryptedLoad("KEY_password")
//        return (cAccount == "" || password == "")
//    }
//
//    // 以下、暗号化してデバイスに保存する(PasswordActivityにも存在するので今後、統一)
//    companion object {
//        const val PREF_NAME = "encrypted_prefs"
//    }
//    // 読み込み
//    private fun encryptedLoad(KEY: String): String {
//        val mainKey = MasterKey.Builder(applicationContext)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val prefs = EncryptedSharedPreferences.create(
//            applicationContext,
//            MainActivity.PREF_NAME,
//            mainKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//        return prefs.getString(KEY, "")!! // nilの場合は空白を返す
//    }
//    // 保存
//    private fun encryptedSave(KEY: String, text: String) {
//        val mainKey = MasterKey.Builder(applicationContext)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val prefs = EncryptedSharedPreferences.create(
//            applicationContext,
//            MainActivity.PREF_NAME,
//            mainKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//        with(prefs.edit()) {
//            putString(KEY, text)
//            apply()
//        }
//    }