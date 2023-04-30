package com.example.tokumemo

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import encrypt
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.URL
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import decrypt


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var weatherText: TextView
    private lateinit var weatherIcon: ImageView
    private lateinit var webViewForLogin: WebView
    private lateinit var menuRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        weatherText = view.findViewById<TextView>(R.id.weather_text)
        weatherIcon = view.findViewById<ImageView>(R.id.weather_icon)
        webViewForLogin = view.findViewById<WebView>(R.id.webView_for_login)
        menuRecyclerView = view.findViewById<RecyclerView>(R.id.menu_recycler_view)


        val contactUs = view.findViewById<Button>(R.id.contact_us)
        contactUs.setOnClickListener { // 意見箱ボタン
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("PAGE_KEY", Url.ContactUs.urlString)
            startActivity(intent)
        }

        // PR画像(広告)の取得
        viewModel.getPRItemsFromGithub()
        recyclerViewInitSetting(view)
        pRImagesInitSetting(view)
//        getWeatherData(view)
        loginWebViewInitSetting(view)

        return view
    }


    /// RecyclerViewの初期設定
    private fun recyclerViewInitSetting(view: View) {
        val displayMenuLists = viewModel.displayMenuList()
        val adapter = HomeMenuRecyclerAdapter(displayMenuLists)
        // 横3列に指定する
        menuRecyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        adapter.setOnBookCellClickListener(object : HomeMenuRecyclerAdapter.OnBookCellClickListener {
            override fun onItemClick(item: HomeListData) {
                if (!DataManager.loginState.completed && item.isLockIconExists) {
                    Toast.makeText(view?.context,
                        "自動ログイン機能をONにしよう！Settingsから試してみてね",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                DataManager.canExecuteJavascript = true
                when(item.id) {
                    // 教務事務システム
                    MenuListItemType.CurrentTermPerformance -> {
                        // API24以下ではこれ API26以上ではLocalDate.now()が使用できる
                        val calendar = Calendar.getInstance()
                        var year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1 //月は0から始まるため、+1する

                        // 1月から3月までは前年度のURLを表示
                        if (month < 4){
                            year -= 1
                        }
                        val intent = Intent(requireContext(), WebActivity::class.java)
                        intent.putExtra("PAGE_KEY",item.url + year)
                        startActivity(intent)
                    }

                    // シラバス
                    MenuListItemType.Syllabus -> {
                        val intent = Intent(requireContext(), PasswordActivity::class.java)
                        intent.putExtra("hogemon", PasswordActivity.DisplayType.Syllabus)
                        startActivity(intent)
                    }

                    // 図書館カレンダー
                    MenuListItemType.LibraryCalendar -> {
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)

                        // ダイアログの表示(常三島と蔵本を)
                        val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext(), R.style.FirstDialogStyle)
                        alertDialog.setTitle("図書館の所在を選択")
                        alertDialog.setMessage("こちらは最新情報ではありません。最新の情報は図書館ホームページをご覧ください。")
                        alertDialog.setNegativeButton("蔵本",
                            DialogInterface.OnClickListener { _, _ ->
                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_$year.pdf&embedded=true"
                                val intent = Intent(requireContext(), WebActivity::class.java)
                                intent.putExtra("PAGE_KEY",libraryURL)
                                startActivity(intent)
                            })
                        alertDialog.setPositiveButton("常三島",
                            DialogInterface.OnClickListener { _, _ ->
                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_$year.pdf&embedded=true"
                                val intent = Intent(requireContext(), WebActivity::class.java)
                                intent.putExtra("PAGE_KEY",libraryURL)
                                startActivity(intent)
                            })
                        alertDialog.show()
                    }

                    // 他のメニューはWebActivityで開く
                    else -> {
                        val intent = Intent(requireContext(), WebActivity::class.java)
                        intent.putExtra("PAGE_KEY",item.url)
                        startActivity(intent)
                    }

                }
            }
        })
        menuRecyclerView.adapter = adapter
    }


    /// PR画像についての初期設定
    private fun pRImagesInitSetting(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.pr_image_button)
        imageView.setOnClickListener {
            // PR画像が表示されているのならdisplayPRImagesNumberには値が入っている
            viewModel.displayPRImagesNumber?.let {
                // 表示されているPR画像の情報をPublicRelationsActivityに値を渡す
                viewModel.prItems[it].let {
                    val intent = Intent(context, PublicRelationsActivity::class.java)
                    intent.putExtra("PR_imageURL",it.imageURL)
                    intent.putExtra("PR_introduction",it.introduction)
                    intent.putExtra("PR_description",it.description)
                    intent.putExtra("PR_tappedURL",it.tappedURL)
                    intent.putExtra("PR_organization_name",it.organization_name)
                    startActivity(intent)
                }
            }
        }

        // PR画像(広告)を10000 msごとに読み込ませる
        Timer().scheduleAtFixedRate(0, 5000) {
            viewModel.selectPRImageNumber()?.let {
                viewModel.displayPRImagesNumber = it
                GetImage(imageView).execute(viewModel.prItems[it].imageURL)
            }
        }
    }


    /// 天気を取得
    private fun getWeatherData(view: View) {
        // 非同期処理
        GlobalScope.launch {
            // 徳島大学本部の所在地の緯度経度
            val latitude = 34.07003444012803 // 緯度(いど)
            val longitude = 134.55981101249947 // 経度(けいど)
            // OpenWeatherMapからAPI通信を許可してもらうKEY
            val apiKey = "e0578cd3fb0d436dd64d4d5d5a404f08"
            // URLを作成 パラメーターに場所と言語、APIのKEYを付与
            val urlStr = "https://api.openweathermap.org/data/2.5/weather?" +
                    "units=" + "metric" + "&" +
                    "lat=" + latitude + "&" +
                    "lon=" + longitude + "&" +
                    "lang=" + "ja" + "&" +
                    "APPID=" + apiKey

            weatherText.text = "取得失敗" // 上書きされなければ、取得失敗ということ(catchでは何度か通る)
            try {
                // URLから返ってきたデータをStringで取得
                val str = URL(urlStr).readText()
                // JSONにパース
                val json = JSONObject(str)

                val weatherData = json.getJSONArray("weather").getJSONObject(0)
                val description = weatherData.getString("description")
                val icon = weatherData.getString("icon")
                val temp = json.getJSONObject("main").getString("temp")

                GetImage(weatherIcon).execute("https://openweathermap.org/img/wn/" + icon + ".png")
                // 1行目に天気の情報(薄い雲など)、2行目に天気の気温を表示
                weatherText.text = "$description\n$temp℃"

            } catch (e: Exception) {
            }
        }
    }


    /// バックグラウンドで動かしているログイン用のWebViewの初期設定
    private fun loginWebViewInitSetting(view: View) {
        // JavaScriptインジェクションを実行するフラグを立てる
        DataManager.canExecuteJavascript = true
        // ログイン処理の開始(iOSでは必要ないが、Androidでは必要。理由不明)
        DataManager.loginState.isProgress = true

        val webView: WebView = view.findViewById(R.id.webView_for_login)
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(Url.UniversityTransitionLogin.urlString)

        webView.webViewClient = object : WebViewClient() {
            // URL読み込み開始前
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                // アンラップ
                val urlString = guard(url) { throw return false }

                // フラグの更新
                checkLoginComplete(urlString)

                // パスワードを登録しているか
                if (!hasRegisteredPassword()) {
                    updateLoginFlag(FlagType.NotStart)
                }

                // タイムアウトの判定
                if (isTimeout(urlString)) {
                    reLogin()
                }

                // 大学Webに自動ログインに失敗していた場合
                if (isLoginFailure(urlString)) {
                    updateLoginFlag(FlagType.LoginFailure)
                    Toast.makeText(view?.context,
                        "学生番号もしくはパスワードが間違っている為、ログインできませんでした",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // ログイン完了時に鍵マークを外す(画像更新)為に、collectionViewのCellデータを更新
                if (DataManager.loginState.completeImmediately) {
                    menuRecyclerView.adapter?.notifyDataSetChanged()
                }

                // ログイン中のアニメーションを消す
//                if (DataManager.loginState.isProgress == false) {
//                    viewActivityIndicator.stopAnimating() // クルクルストップ
//                    loginGrayBackGroundView.isHidden = true
//                }
                return false
            }

            // 読み込み完了時
            override fun onPageFinished(view: WebView?, url: String?) {
                // アンラップ
                val urlString = guard(url) { throw return }

                // JavaScriptを動かしたいURLかどうかを判定し、必要なら動かす
                if (canExecuteJS(urlString)) {
                    val view = guard(view) {
                        throw return
                    }
                    val cAccount = getPassword(view.context, "KEY_cAccount")
                    val password = getPassword(view.context, "KEY_password")

                    // 徳島大学　統合認証システムサイト(ログインサイト)に自動ログインを行う。JavaScriptInjection
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

                    // フラグを更新
                    updateLoginFlag(FlagType.ExecutedJavaScript)
                    return
                }
            }
        }
    }

    /// 大学統合認証システム(IAS)のページを読み込む
    /// ログインの処理はWebViewのdidFinishで行う
    private fun reLogin() {
//        viewActivityIndicator.startAnimating() // クルクルスタート
//        loginGrayBackGroundView.isHidden = false
        updateLoginFlag(FlagType.LoginStart)
        webViewForLogin.loadUrl(Url.UniversityTransitionLogin.urlString)
    }


    /*
     * 以下はViewModelに記述したいが、SharedPreferencesの扱いに慣れていない為、一時的にここに入れる
     */

    /// 大学統合認証システム(IAS)へのログインが完了したか判定
    private fun checkLoginComplete(urlStr: String) {
        // ログイン後のURL
        val check1 = urlStr.contains(Url.SkipReminder.urlString)
        val check2 = urlStr.contains(Url.CourseManagementPC.urlString)
        val check3 = urlStr.contains(Url.CourseManagementMobile.urlString)
        // 上記から1つでもtrueがあれば、result = true
        val result = (check1 || check2 || check3)

        // ログイン処理中かつ、ログイン後のURL
        if (DataManager.loginState.isProgress && result) {
            updateLoginFlag(FlagType.LoginSuccess)
            return
        }
        return
    }

    enum class FlagType {
        NotStart,
        LoginStart,
        LoginSuccess,
        LoginFailure,
        ExecutedJavaScript
    }
    // Dos攻撃を防ぐ為、1度ログインに失敗したら、JavaScriptを動かすフラグを下ろす
    private fun updateLoginFlag(type: FlagType) {
        when (type){
            FlagType.NotStart -> {
                DataManager.canExecuteJavascript = false
                DataManager.loginState.isProgress = false
                DataManager.loginState.completed = false
            }
            FlagType.LoginStart -> {
                DataManager.canExecuteJavascript = true // ログイン用のJavaScriptを動かす
                DataManager.loginState.isProgress = true // ログイン処理中
                DataManager.loginState.completed = false // ログインが完了していない
            }
            FlagType.LoginSuccess -> {
                DataManager.canExecuteJavascript = false
                DataManager.loginState.isProgress = false
                DataManager.loginState.completeImmediately = true
                DataManager.loginState.completed = true
//                lastLoginTime = Date() // 最終ログイン時刻の記録
            }
            FlagType.LoginFailure -> {
                DataManager.canExecuteJavascript = false
                DataManager.loginState.isProgress = false
                DataManager.loginState.completed = false
            }
            FlagType.ExecutedJavaScript -> {
                // Dos攻撃を防ぐ為、1度ログインに失敗したら、JavaScriptを動かすフラグを下ろす
                DataManager.canExecuteJavascript = false
                DataManager.loginState.isProgress = true
                DataManager.loginState.completed = false
            }
        }
    }

    // 学生番号、パスワードを登録しているか判定
    private fun hasRegisteredPassword(): Boolean {
        val view = guard(view) { throw return false}
        val cAccount = getPassword(view.context,"KEY_cAccount")
        val password = getPassword(view.context,"KEY_password")

        return !(cAccount == null || password == null)
    }

    /// 最新の利用規約同意者か判定し、同意画面の表示を行うべきか判定
//    private fun shouldShowTermsAgreementView(): Boolean {
//        return (DataManager.agreementVersion != ConstStruct.latestTermsVersion)
//    }

    /// タイムアウトのURLであるか判定
    private fun isTimeout(urlStr: String): Boolean {
        return urlStr == Url.UniversityServiceTimeOut.urlString || urlStr == Url.UniversityServiceTimeOut2.urlString
    }

    /// 大学統合認証システム(IAS)へのログインが失敗したか判定
    private fun isLoginFailure(urlStr: String): Boolean {
        // ログイン失敗した時のURL
        val check1 = urlStr.contains(Url.UniversityLogin.toString())
        val check2 = (urlStr.takeLast(2) != "s1")
        // 上記からどちらもtrueであれば、result = true
        val result = check1 && check2

        // ログイン処理中かつ、ログイン失敗した時のURL
        if (DataManager.loginState.isProgress && result) {
            DataManager.loginState.isProgress = false
            return true
        }
        return false
    }

    /// JavaScriptを動かしたい指定のURLか判定
    private fun canExecuteJS(urlString: String): Boolean {
        // JavaScriptを実行するフラグ
        if (DataManager.canExecuteJavascript == false) { return false }

        // cアカウント、パスワードの登録確認
        if (hasRegisteredPassword() == false) { return false }

        // 大学統合認証システム(IAS)のログイン画面の判定
        if (urlString.contains(Url.UniversityLogin.urlString)) { return true }

        // それ以外なら
        return false
    }

    // パスワードを暗号化して保存する
    private fun savePassword(context: Context, plainPassword: String, KEY: String) {
        // 暗号化
        val encryptedPassword = encrypt(context, "password", plainPassword)

        // shaaredPreferencesに保存
        var editor = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
            putString(KEY, encryptedPassword).commit()
        }
    }

    // パスワードを復号化して取得する
    private fun getPassword(context: Context, KEY: String): String? {
        // shaaredPreferencesから読み込み
        val encryptedPassword = context.getSharedPreferences("my_settings", Context.MODE_PRIVATE).getString(KEY, null)
        // アンラップ
        encryptedPassword ?: return null
        // 復号化
        return decrypt("password", encryptedPassword)
    }
}