package com.example.tokumemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]


        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.settings_recycler_view)
        listView.adapter = SettingsListsAdapter(requireContext(), viewModel.initSettingsList)
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