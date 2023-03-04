package com.example.tokumemo.main.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R
import com.example.tokumemo.main.news.Data
import com.example.tokumemo.web.WebActivity
import com.example.tokumemo.web.WebViewModel


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        var titleArray = arrayListOf<Data>()
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

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
        return view
    }
}