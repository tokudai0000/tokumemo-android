package com.example.tokumemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        var titleArray = arrayListOf<NewsListData>()
        titleArray.add(NewsListData().apply {
            title = "パスワード"
            pubDate = ""
            link = ""
        })
        titleArray.add(NewsListData().apply {
            title = "このアプリについて"
            pubDate = ""
            link = ""
        })
        titleArray.add(NewsListData().apply {
            title = "カスタマイズ"
            pubDate = ""
            link = ""
        })

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.settings_recycler_view)
        listView.adapter = NewsListViewAdapter(requireContext(), titleArray)
//        val adapter = SettingsListsAdapter(titleArray)

        // xmlにて実装したListViewの取得

        // 項目をタップしたときの処理
//        listView.setOnItemClickListener {parent, view, position, id ->
//
//            // 項目のラベルテキストをログに表示
//            Log.i("PRINT", titleArray[position].link.toString())
//
//            val intent = Intent(requireContext(), WebActivity::class.java)
//            // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
//            intent.putExtra("PAGE_KEY",titleArray[position].link.toString())
//            startActivity(intent)
//        }
//        listView.adapter

        return view
    }
}