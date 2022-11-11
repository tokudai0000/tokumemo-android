package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tokumemo.databinding.ActivityMainBinding
import com.example.tokumemo.databinding.ActivityNewsBinding
import com.example.tokumemo.flag.MainModel
import com.example.tokumemo.manager.DataManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

class NewsActivity : AppCompatActivity() {
    lateinit var binding : ActivityNewsBinding
    private lateinit var webView: WebView
    private lateinit var viewModel: MainModel
    private lateinit var titleArray: Array<String>

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        titleArray = arrayOf("")
        val listView = findViewById<ListView>(R.id.newsList)

        Thread{
            getNews()
        }.start()
//        getNews()

        Thread.sleep(1000)

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, titleArray)
        listView.adapter = adapter

//        binding.textView3.text = resultText

        // OnItemClickListenerを実装
        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, titleArray[position], Toast.LENGTH_SHORT).show()
        }

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

//        binding.button.setOnClickListener {
//            resultText = ""
//            // 天気と時刻を取得
//            getNews()
//            // 3秒間処理を止める
//            Thread.sleep(3000)
//            // 結果をtextViewに表示
//            binding.newsTitle.text = resultText
//
//        }

//        webView = findViewById(R.id.webView)
//        webView.settings.javaScriptEnabled = true
//        viewModel = ViewModelProvider(this).get(MainModel::class.java)
//
//        // 検索アプリで開かない
//        webView.webViewClient = WebViewClient()
//
//        // 読み込み時にページ横幅を画面幅に無理やり合わせる
//        webView.getSettings().setLoadWithOverviewMode( true );
//        // ワイドビューポートへの対応
//        webView.getSettings().setUseWideViewPort( true );
//        // 拡大縮小対応
//        webView.getSettings().setBuiltInZoomControls(true);
//
//        webView.loadUrl("https://www.tokushima-u.ac.jp/recent/")
    }


    @RequiresApi(Build.VERSION_CODES.N)
    private fun getNews(): Job = GlobalScope.launch {
        titleArray = arrayOf("")
        // 結果を初期化
        // URL。場所と言語・API_KEYを添付
        var API_URL = "https://api.rss2json.com/v1/api.json?rss_url=https://www.tokushima-u.ac.jp/recent/rss.xml"
        var url = URL(API_URL)
        //APIから情報を取得する.
        var br = BufferedReader(InputStreamReader(url.openStream()))
        // 所得した情報を文字列化
        var str = br.readText()
        //json形式のデータとして識別
        var json = JSONObject(str)

        var itemListNum = json.getJSONArray("items").length()

        if (itemListNum >= 1) {
            for (i in 0..itemListNum - 1) {
                var itemList = json.getJSONArray("items").getJSONObject(i.toInt())
                var title = itemList.getString("title")
                var link = itemList.getString("link")
                if (i == 1){
                    titleArray[0] = "$title"
                }else{
                    titleArray += "$title"
                }
            }
        }
    }
}