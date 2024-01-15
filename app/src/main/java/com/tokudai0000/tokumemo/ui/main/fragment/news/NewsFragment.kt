package com.tokudai0000.tokumemo.ui.main.fragment.news

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.viewModels
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.ui.web.WebActivity

class NewsFragment : Fragment() {

    private val viewModel by viewModels<NewsViewModel>()

    private lateinit var listView: ListView
    private lateinit var adapter: NewsListViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_news, container, false)

        listView = view.findViewById<ListView>(R.id.list_view)
        listView.setOnItemClickListener {_, _, position, _ ->
            val newsItem = viewModel.newsItems.value?.get(position)
            newsItem?.let {
                val intent = Intent(requireContext(), WebActivity::class.java)
                intent.putExtra("PAGE_KEY", it.link.toString())
                startActivity(intent)
            }
        }

        // Newsデータ取得
        viewModel.newsItems.observe(viewLifecycleOwner) { newsList ->
            listView.adapter = NewsListViewAdapter(requireContext(), newsList)
        }
        viewModel.getNewsRSS()

        return view
    }
}

