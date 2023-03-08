package com.example.tokumemo.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.GetImage
import com.example.tokumemo.R
import com.example.tokumemo.model.DataManager
import com.example.tokumemo.model.Url
import com.example.tokumemo.web.WebActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var weatherText: TextView
    private lateinit var weatherIcon: ImageView


    private var resultText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        weatherText = view.findViewById<TextView>(R.id.weatherText)
        weatherIcon = view.findViewById<ImageView>(R.id.weatherIcon)
        val studentCardButton = view.findViewById<Button>(R.id.studentCard)
        studentCardButton.setOnClickListener {
            val intent = Intent(requireContext(), WebActivity::class.java)
            // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
            intent.putExtra("PAGE_KEY", Url.contactUs.urlString)
            startActivity(intent)
        }

        viewModel.getPRItems()
        listViewInitSetting(view)
        adImagesRotationTimerON(view)

        getWeatherNews(view)


        return view

    }


    private fun listViewInitSetting(view: View) {

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<RecyclerView>(R.id.menu_recycler_view)
        val adapter = MenuListsAdapter(viewModel.initMenuList)
        listView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        // 書籍情報セルのクリック処理
        adapter.setOnBookCellClickListener(
            object : MenuListsAdapter.OnBookCellClickListener {
                override fun onItemClick(book: MenuData) {
                    // 書籍データを渡す処理
//                    setFragmentResult("menuData", bundleOf(
//                        "id" to book.id,
//                        "url" to book.url
//                    ))

                    val intent = Intent(requireContext(), WebActivity::class.java)
                    // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
                    intent.putExtra("PAGE_KEY",book.url)
                    startActivity(intent)
                    // 画面遷移処理
//                    parentFragmentManager
//                        .beginTransaction()
//                        .replace(R.id.fl_activity_main, BookFragment())
//                        .addToBackStack(null)
//                        .commit()
                }
            }
        )
        listView.adapter = adapter
    }

    /// 広告を一定時間ごとに読み込ませる処理のスイッチ
    private fun adImagesRotationTimerON(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.pr_image_button)

        // ImageViewにクリックリスナーを設定
        imageView.setOnClickListener {
            viewModel.displayPRImagesNumber?.let {
                viewModel.prItems[it].let {
                    val intent = Intent(context, PRActivity::class.java)
                    intent.putExtra("PR_imageURL",it.imageURL)
                    intent.putExtra("PR_introduction",it.introduction)
                    intent.putExtra("PR_description",it.description)
                    intent.putExtra("PR_tappedURL",it.tappedURL)
                    intent.putExtra("PR_organization_name",it.organization_name)
                    startActivity(intent)
                }
            }
        }

        // 5000 ms 毎に実行
        Timer().scheduleAtFixedRate(0, 5000) {
            val num = viewModel.selectPRImageNumber()
            if (num != null) {
                viewModel.displayPRImagesNumber = num
                val imageTask: GetImage = GetImage(imageView)
                imageTask.execute(viewModel.prItems[num].imageURL)
            }
        }
    }

    // 天気を取得
    private fun getWeatherNews(view: View): Job = GlobalScope.launch {
        // 徳島大学本部の所在地の緯度経度
        var lat = 34.07003444012803
        var lon = 134.55981101249947
        // APIを使う際に必要なKEY
        val API_KEY = "e0578cd3fb0d436dd64d4d5d5a404f08"
        // URL。場所と言語・API_KEYを添付
        val urlStr = "https://api.openweathermap.org/data/2.5/weather?" +
                "units=metric&" +
                "lat=" + lat + "&" +
                "lon=" + lon + "&" +
                "lang=" + "ja" + "&" +
                "APPID=" + API_KEY

        try {
            val str = URL(urlStr).readText()
            val json = JSONObject(str)

            // 天気を取得
            val weatherList = json.getJSONArray("weather").getJSONObject(0)

            // 天気を取得
            val descriptionText = weatherList.getString("description")
            val icon = weatherList.getString("icon")
            val temp = json.getJSONObject("main").getString("temp")

            val imageTask: GetImage = GetImage(weatherIcon)
            imageTask.execute("https://openweathermap.org/img/wn/" + icon + ".png")
            weatherText.text = "$descriptionText\n$temp℃"
        } catch (e: Exception) {
//            weatherText.text = "現在天気を取得できません"
        }
    }

}