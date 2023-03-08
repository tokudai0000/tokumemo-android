package com.example.tokumemo.main.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R
import com.example.tokumemo.main.home.HomeViewModel
import com.example.tokumemo.main.news.CustomAdapter
import com.example.tokumemo.main.news.Data
import com.example.tokumemo.web.WebActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        var titleArray = arrayListOf<Data>()
        titleArray.add(Data().apply {
            title = "パスワード"
            pubDate = ""
            link = ""
        })
        titleArray.add(Data().apply {
            title = "このアプリについて"
            pubDate = ""
            link = ""
        })
        titleArray.add(Data().apply {
            title = "カスタマイズ"
            pubDate = ""
            link = ""
        })

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.settings_recycler_view)
        listView.adapter = CustomAdapter(requireContext(), titleArray)
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