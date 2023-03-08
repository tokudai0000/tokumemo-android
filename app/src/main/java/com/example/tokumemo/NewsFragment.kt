package com.example.tokumemo

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.*
import org.json.JSONArray
import org.json.JSONObject

class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view =  inflater.inflate(R.layout.fragment_news, container, false)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]
        listView = view.findViewById<ListView>(R.id.list_view)

        listView.setOnItemClickListener {_, _, position, _ ->
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("PAGE_KEY",viewModel.newsItems[position].link.toString())
            startActivity(intent)
        }

        // RSSをJsonに変換してくれるサイトから、データを取得。
        // 本来はViewModelに記載したいが、通信完了後の書き方を知らないため、後日修正予定
        val url = Url.rss.urlString
        url.httpGet().responseJson { _, _, result ->
            when (result) {
                is Result.Success -> { // 通信完了
                    val items:JSONArray = result.get().obj().getJSONArray("items")

                    for(i in 0 until items.length())
                        viewModel.newsItems.add(Data().apply {
                            title = items.getJSONObject(i)["title"].toString()
                            pubDate = items.getJSONObject(i)["pubDate"].toString()
                            link = items.getJSONObject(i)["link"].toString()
                        })

                }
                is Result.Failure -> { // 通信失敗
                    // エラーハンドリング未実装
                    val ex = result.getException()
                    JSONObject(mapOf("message" to ex.toString()))
                }
            }

            // requireContext() : nullを許容しない
            listView.adapter = CustomAdapter(requireContext(), viewModel.newsItems)
        }

        return view
    }
}

