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
import com.example.tokumemo.main.news.Data
import com.example.tokumemo.web.WebActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        var twoDimensionalArray = arrayOf<Array<String>>()
        twoDimensionalArray += arrayOf<String>("パスワード")
        twoDimensionalArray += arrayOf<String>("このアプリについて","お問い合わせ","公式SNS","ホームページ")
        twoDimensionalArray += arrayOf<String>("利用規約","プライバシーポリシー")
//        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<RecyclerView>(R.id.settings_recycler_view)

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
        listView.adapter

        return view
    }
}