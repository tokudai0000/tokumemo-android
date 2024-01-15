package com.tokudai0000.tokumemo.ui.main.fragment.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.domain.model.NewsListData

class NewsListViewAdapter(context: Context, private val items: ArrayList<NewsListData>) : ArrayAdapter<NewsListData>(context, R.layout.layout_news_list, items) {

    private class ViewHolder(view: View) {
        val textView: TextView = view.findViewById(R.id.item_title)
        val pubDate: TextView = view.findViewById(R.id.item_text)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_news_list, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val item = items[position]
        viewHolder.textView.text = item.title
        viewHolder.pubDate.text = item.pubDate

        return view
    }
}
