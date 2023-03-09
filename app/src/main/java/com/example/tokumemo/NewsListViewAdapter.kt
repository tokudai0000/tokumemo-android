package com.example.tokumemo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NewsListViewAdapter(context: Context, private val items: ArrayList<NewsListData>) : ArrayAdapter<NewsListData>(context,
    R.layout.item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)

        val textView = view?.findViewById<TextView>(R.id.item_title)
        val pubDate = view?.findViewById<TextView>(R.id.item_text)

        val item = items[position]
        textView?.text = item.title
        pubDate?.text = item.pubDate

        return view!!
    }

}