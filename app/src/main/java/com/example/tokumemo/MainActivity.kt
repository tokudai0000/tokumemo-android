package com.example.tokumemo

import FirstDialogFragment
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.databinding.ActivityMainBinding
import com.example.tokumemo.flag.MainModel
import com.example.tokumemo.manager.DataManager
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel
    private lateinit var weatherWebView: WebView
    private lateinit var weatherViewModel: MainModel
    private var urlString = ""

    lateinit var binding : ActivityMainBinding
    private var resultText = ""
    // 徳島大学本部の所在地の緯度経度
    private var placeLat = 34.07003444012803
    private var placeLon = 134.55981101249947

    private var iconUrl = ""
    private var isConnectToNetwork = false

    override fun onBackPressed() {
        // Android戻るボタン無効
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId", "SetJavaScriptEnabled", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentYearSdf = SimpleDateFormat("yyyy")
        val currentYear = currentYearSdf.format(Date())
        val currentMonthSdf = SimpleDateFormat("MM")
        val currentMonth = currentMonthSdf.format(Date())

        // ネット接続できているか
        // ConnectivityManagerの取得
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // NetworkCapabilitiesの取得
        // 引数にcm.activeNetworkを指定し、現在アクティブなデフォルトネットワークに対応するNetworkオブジェクトを渡している
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            isConnectToNetwork = true
        }

        // 初回起動時に利用規約ダイアログを表示
        if (encryptedLoad("isFirstTime") != "false") {
            val dialog = FirstDialogFragment()
            dialog.show(supportFragmentManager, "simple")
            encryptedSave("isFirstTime", "false")
        }
        if (isConnectToNetwork){
            // 天気情報表示準備(実際に表示するのは隠れWebビューのonPageFinishedの最後あたり)

            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            // 天気を取得
            getWeatherNews()
            initWeatherWebIcon()

            // 隠れWebビューここから（ここで先にログイン処理のみしておく）
            webView = findViewById(R.id.loginView)
            webView.settings.javaScriptEnabled = true
            viewModel = ViewModelProvider(this)[MainModel::class.java]

            // テストユーザーの場合はjsCountを-1にしておく
    //        if (encryptedLoad("KEY_studentNumber") == "0123456789" && encryptedLoad("KEY_password") == "0000"){
    //            DataManager.jsCount = -1
    //        } else {
    //            DataManager.jsCount = 0
    //        }
            DataManager.jsCount = 0

            // 検索アプリで開かない
            webView.webViewClient = object : WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    if (url != null) {
                        urlString = url
                    }

                    when (viewModel.anyJavaScriptExecute(urlString)) {
                        MainModel.JavaScriptType.loginIAS -> {

    //                        if (shouldShowPasswordView() && encryptedLoad("isFirstTime") == "false") {
    //                            // パスワード登録画面を表示
    //                            val intent = Intent(applicationContext, PasswordActivity::class.java)
    //                            startActivity(intent)
    //                        }
                            if (DataManager.canExecuteJavascript && DataManager.jsCount >= 0) {
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
                                }
                            }
                        }
                        else -> {}
                    }

                    super.onPageFinished(view, url)

                    // ついでに天気情報を更新しておく
                    binding.weatherText.text = resultText
                    iconUrl = "https://openweathermap.org/img/wn/" + encryptedLoad("icon") + ".png"
                    weatherWebView.loadUrl(iconUrl)
                }
            }
            webView.loadUrl("https://eweb.stud.tokushima-u.ac.jp/Portal/")
            // 隠れWebビューここまで
        }
        
        // 学生証バーコード生成
        createStudentCard()

        // メニューバー
        val Home = findViewById<Button>(R.id.home)
        Home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            DataManager.canExecuteJavascript = true
            finish()
        }

        val News = findViewById<Button>(R.id.news)
        News.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val Review = findViewById<Button>(R.id.review)
        Review.setOnClickListener{
            val intent = Intent(this, ReviewActivity::class.java)
            startActivity(intent)
            finish()
        }

        val Others = findViewById<Button>(R.id.others)
        Others.setOnClickListener{
            val intent = Intent(this, OthersActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 天気を押したとき
        val weather = findViewById<Button>(R.id.weatherButton)
        val weatherIcon = findViewById<Button>(R.id.weatherButton2)
        weather.setOnClickListener{
            goWeb("https://www.jma.go.jp/bosai/forecast/#area_type=class20s&area_code=3620100")
        }
        weatherIcon.setOnClickListener{
            goWeb("https://www.jma.go.jp/bosai/forecast/#area_type=class20s&area_code=3620100")
        }

        // 教務システムを押したとき
        val Button2 = findViewById<Button>(R.id.academicAffairsSystem)
        Button2.setOnClickListener{
            goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx")
        }
        // マナバを押したとき
        val Button1 = findViewById<Button>(R.id.manaba)
        Button1.setOnClickListener{
            goWeb("https://manaba.lms.tokushima-u.ac.jp/ct/home")
        }
        // メールを押したとき
        val Button3 = findViewById<Button>(R.id.email)
        Button3.setOnClickListener{
            goWeb("https://outlook.office365.com/mail/")
        }
        // 図書館カレンダーを押したとき
        val libraryCarender = findViewById<Button>(R.id.libraryCarender)
        libraryCarender.setOnClickListener{
            val term = if (currentMonth.toInt() <= 3){
                (currentYear.toInt()-1).toString()
            }else{
                currentYear
            }
            // ダイアログの表示
            val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this, R.style.FirstDialogStyle)
            alertDialog.setTitle("図書館の所在地")
//            alertDialog.setMessage("メッセージ")
            alertDialog.setPositiveButton("常三島",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_$term.pdf&embedded=true"
                    goWeb(libraryURL)
                })
            alertDialog.setNegativeButton("蔵本",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_$term.pdf&embedded=true"
                    goWeb(libraryURL)
                })
            alertDialog.setOnCancelListener(DialogInterface.OnCancelListener {
                // キャンセルの処理
            })
            alertDialog.show()
        }

        // 図書検索を押したとき
        val Button4 = findViewById<Button>(R.id.library)
        Button4.setOnClickListener{
            goWeb("https://opac.lib.tokushima-u.ac.jp/opac/user/top")
        }
        // 図書貸出延長を押したとき
        val libraryExtension = findViewById<Button>(R.id.libraryExtension)
        libraryExtension.setOnClickListener{
            goWeb("https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings")
        }
        // 生協を押したとき
        val Button5 = findViewById<Button>(R.id.seikyou)
        Button5.setOnClickListener{
            goWeb("https://vsign.jp/tokudai/maruco")
        }
        // 時間割を押したとき
        val Button7 = findViewById<Button>(R.id.timetable)
        Button7.setOnClickListener{
            goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx")
        }
        // 総合認証ポータルを押したとき
        val portal = findViewById<Button>(R.id.portal)
        portal.setOnClickListener{
            goWeb("https://my.ait.tokushima-u.ac.jp/portal/")
        }
        // 今学期の成績を押したとき
        val Button6 = findViewById<Button>(R.id.result)
        Button6.setOnClickListener{
            var resultURL = ""
            resultURL = if (currentMonth.toInt() <= 3){
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year=" + (currentYear.toInt()-1).toString()
            }else{
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year=$currentYear"
            }
            goWeb(resultURL)
        }
        // 全学期の成績を押したとき
        val resultAll = findViewById<Button>(R.id.resultAll)
        resultAll.setOnClickListener{
            goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Menu.aspx")
        }
        // シラバスを押したとき
        val Button8 = findViewById<Button>(R.id.syllabus)
        Button8.setOnClickListener{
            goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/SearchMain.aspx")
        }
        // キャリアセンターを押したとき
        val Button0 = findViewById<Button>(R.id.careerCenter)
        Button0.setOnClickListener{
            goWeb("https://www.tokudai-syusyoku.com/index.php")
        }
        // 大学サイトを押したとき
        val univWebsite = findViewById<Button>(R.id.univWebsite)
        univWebsite.setOnClickListener{
            goWeb("https://www.tokushima-u.ac.jp/")
        }
        // 図書館サイトを押したとき
        val libraryHome = findViewById<Button>(R.id.libraryHome)
        libraryHome.setOnClickListener{
                goWeb("https://opac.lib.tokushima-u.ac.jp/drupal/")
            }
        // 本購入を押したとき
        val bookPurchase = findViewById<Button>(R.id.bookPurchase)
        bookPurchase.setOnClickListener{
                goWeb("https://opac.lib.tokushima-u.ac.jp/opac/user/purchase_requests/new")
            }
        // 出欠を押したとき
        val attendance = findViewById<Button>(R.id.attendance)
        attendance.setOnClickListener{
                goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Attendance/AttendList.aspx")
            }
        // 授業アンケートを押したとき
        val questionnaire = findViewById<Button>(R.id.questionnaire)
        questionnaire.setOnClickListener{
                goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Enquete/EnqAnswerList.aspx")
            }
        // LMS一覧を押したとき
        val LMS = findViewById<Button>(R.id.LMS)
        LMS.setOnClickListener{
                goWeb("https://uls01.ulc.tokushima-u.ac.jp/info/index.html")
            }
        // 常三島図書館HPを押したとき
        val libraryHomeJosanjima = findViewById<Button>(R.id.libraryHomeJosanjima)
        libraryHomeJosanjima.setOnClickListener{
                goWeb("https://www.lib.tokushima-u.ac.jp/")
            }
        // 蔵本図書館HPを押したとき
        val libraryHomeKuramoto = findViewById<Button>(R.id.libraryHomeKuramoto)
        libraryHomeKuramoto.setOnClickListener{
                goWeb("https://www.lib.tokushima-u.ac.jp/kura.shtml")
            }
        // 教務システム_PCを押したとき
        val academicAffairsSystemPC = findViewById<Button>(R.id.academicAffairsSystemPC)
        academicAffairsSystemPC.setOnClickListener{
                goWeb("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx")
            }
        // マナバ_モバイルを押したとき
        val manabaMob = findViewById<Button>(R.id.manabaMob)
        manabaMob.setOnClickListener{
                goWeb("https://manaba.lms.tokushima-u.ac.jp/s/home_summary")
            }
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
            MainActivity.PREF_NAME,
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
            MainActivity.PREF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        with(prefs.edit()) {
            putString(KEY, text)
            apply()
        }
    }

    // 押されたWebサイトにとぶ
    private fun goWeb(pageId: String) {
        val intent = Intent(this, WebActivity::class.java)
        // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
        intent.putExtra("PAGE_KEY",pageId)
        // 自動入力のフラグを上げる
        DataManager.canExecuteJavascript = true
        startActivity(intent)
    }

    // unixtimeからフォーマットの日付に変換
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun unixTimeChange(unixTime: String): String {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH")
        val nowTime = Date(unixTime.toInt() * 1000L)
        return sdf.format(nowTime)
    }

    // 天気を取得
    @OptIn(DelicateCoroutinesApi::class)
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getWeatherNews(): Job = GlobalScope.launch {
        val sdf = SimpleDateFormat("yyyy/MM/dd HH")
        val currentTime = sdf.format(Date())

        if (currentTime != encryptedLoad("dateTime")) {
            val sdfForText = SimpleDateFormat("yyyy/MM/dd")
            val currentTimeForText = sdfForText.format(Date())
            // 結果を初期化
            resultText = ""
            // APIを使う際に必要なKEY
            val API_KEY = "e0578cd3fb0d436dd64d4d5d5a404f08"
            // URL。場所と言語・API_KEYを添付
            val API_URL = "https://api.openweathermap.org/data/2.5/weather?" +
                    "units=metric&" +
                    "lat=" + placeLat + "&" +
                    "lon=" + placeLon + "&" +
                    "lang=" + "ja" + "&" +
                    "APPID=" + API_KEY
            val url = URL(API_URL)
            //APIから情報を取得する.
            val br = BufferedReader(InputStreamReader(withContext(Dispatchers.IO) {
                url.openStream()
            }))
            // 所得した情報を文字列化
            val str = br.readText()
            //json形式のデータとして識別
            val json = JSONObject(str)

            // 天気を取得
            val weatherList = json.getJSONArray("weather").getJSONObject(0)
            // unixtime形式で保持されている時刻を取得
            val time = json.getString("dt")
            // 天気を取得
            val descriptionText = weatherList.getString("description")
            val icon = weatherList.getString("icon")
            val temp = json.getJSONObject("main").getString("temp")
            encryptedSave("dateTime", unixTimeChange(time))
            encryptedSave("dateTimeForText", currentTimeForText)
            encryptedSave("descriptionText", descriptionText)
            encryptedSave("icon", icon)
            encryptedSave("temp", temp)
            Log.i("weatherList: ", weatherList.toString())
            resultText += "$descriptionText\n$temp℃"
        } else {
            resultText += "${encryptedLoad("descriptionText")}\n${encryptedLoad("temp")}℃"
        }

    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWeatherWebIcon(){
        weatherWebView = findViewById(R.id.weatherIcon)
        weatherWebView.settings.javaScriptEnabled = true
        weatherViewModel = ViewModelProvider(this).get(MainModel::class.java)
        weatherWebView.webViewClient = WebViewClient()
        // 読み込み時にページ横幅を画面幅に無理やり合わせる
        weatherWebView.settings.loadWithOverviewMode = true
        // ワイドビューポートへの対応
        weatherWebView.settings.useWideViewPort = true
    }

    // 学生証を表示
    @SuppressLint("SetTextI18n")
    private fun createStudentCard(){
        val barCode = findViewById<ImageView>(R.id.barCode)
        val createBarCode = findViewById<Button>(R.id.studentCard)
        val homeScreen = findViewById<LinearLayout>(R.id.homeScreen)
        val studentCardView = findViewById<ConstraintLayout>(R.id.studentCardView)
//        生成ボタンのクリックイベントを設定
        createBarCode.setOnClickListener {
            studentCardView.visibility = View.VISIBLE
            homeScreen.visibility = View.INVISIBLE
            val multiFormatWriter = MultiFormatWriter()
            try {
                val bitMatrix =
                    multiFormatWriter.encode(encryptedLoad("KEY_studentNumber")+"0", BarcodeFormat.CODABAR, 500, 200)
                val barcodeEncoder = BarcodeEncoder()
                val bitmap = barcodeEncoder.createBitmap(bitMatrix)
                barCode.setImageBitmap(bitmap)

                binding.studentCardCode.text = "A"+encryptedLoad("KEY_studentNumber")+"0A"
            } catch (_: Exception) {
            }
        }
        val back = findViewById<Button>(R.id.backButton)
        back.setOnClickListener{
            studentCardView.visibility = View.INVISIBLE
            homeScreen.visibility = View.VISIBLE
        }
    }
}