package com.example.tokumemo.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R
import com.example.tokumemo.main.news.Data


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        var titleArray = arrayListOf<Data>()

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<RecyclerView>(R.id.menu_recycler_view)

        listView.adapter = MenuListsAdapter(listOf(MenuData(title = "琵琶湖"), MenuData(title = "霞ヶ浦"), MenuData(title = "相模湖")))
        listView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)
        return view
    }
}