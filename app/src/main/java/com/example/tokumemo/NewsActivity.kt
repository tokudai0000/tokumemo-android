package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.tokumemo.databinding.ActivityNewsBinding
import com.example.tokumemo.manager.DataManager
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class NewsActivity : AppCompatActivity() {
    lateinit var binding : ActivityNewsBinding
    private lateinit var titleArray: Array<String>
    private lateinit var linkArray: Array<String>
    private var isConnectToNetwork = false

    override fun onBackPressed() {
        // Android戻るボタン無効
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        // ネット接続できているか
        // ConnectivityManagerの取得
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        // NetworkCapabilitiesの取得
        // 引数にcm.activeNetworkを指定し、現在アクティブなデフォルトネットワークに対応するNetworkオブジェクトを渡している
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        if (capabilities != null) {
            isConnectToNetwork = true
        }

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleArray = arrayOf("ニュースを取得できませんでした。「NEWS」をもう一度タップして更新してください。")
        linkArray = arrayOf("")
        val listView = findViewById<ListView>(R.id.newsList)

        // ニュース取得
        if (isConnectToNetwork) {
            Thread {
                getNews()
            }.start()

            Thread.sleep(1000)
            val adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArray)
            listView.adapter = adapter

            // 選んだニュースを表示
            listView.setOnItemClickListener { parent, view, position, id ->

                val url = linkArray[position]
                goWeb(url)
            }
        } else {
            titleArray = arrayOf("ネットが接続されていません。")
            val adapter =
                ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArray)
            listView.adapter = adapter
        }

        // メニューバー
        val home = findViewById<Button>(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

    // ニュース取得
    @OptIn(DelicateCoroutinesApi::class)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun getNews(): Job = GlobalScope.launch {
        titleArray = arrayOf("ニュースを取得できませんでした。「NEWS」をもう一度タップして更新してください。")
        linkArray = arrayOf("") // 結果を初期化
        try {
            // URL。場所と言語・API_KEYを添付
            val API_URL = "https://api.rss2json.com/v1/api.json?rss_url=https://www.tokushima-u.ac.jp/recent/rss.xml"
            val url = URL(API_URL)
            //APIから情報を取得する.
            val br = BufferedReader(InputStreamReader(withContext(Dispatchers.IO) {
                url.openStream()
            }))
            // 所得した情報を文字列化
            val str = br.readText()
            //json形式のデータとして識別
            val json = JSONObject(str)

            val itemListNum = json.getJSONArray("items").length()

            if (itemListNum >= 1) {
                for (i in 0 until itemListNum) {
                    val itemList = json.getJSONArray("items").getJSONObject(i.toInt())
                    val title = itemList.getString("title")
                    val link = itemList.getString("link")
                    if (i == 1){
                        titleArray[0] = title
                        linkArray[0] = link
                    }else{
                        titleArray += title
                        linkArray += link
                    }
                }
            }
        } catch (e: Exception) {
            titleArray[0] = "申し訳ございません、現在ニュースを取得できません。"
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
}