package com.example.tokumemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

class News : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_news, container, false)

        // 配列の生成
        val array = arrayOf("リスト１", "リスト２", "リスト３", "リスト４", "リスト５",)

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.list_view)

        // ArrayAdapterの生成
        val adapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, array)

        // ListViewに、生成したAdapterを設定
        listView.adapter = adapter

        return view
    }

}