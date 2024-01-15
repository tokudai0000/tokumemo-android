package com.tokudai0000.tokumemo.ui.main.fragment.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.domain.model.SettingsItem

class SettingsListViewAdapter(context: Context,private val items: List<SettingsItem>) : ArrayAdapter<SettingsItem>(context, R.layout.layout_settings_item, items) {

    private class ViewHolder(view: View) {
        val textView: TextView = view.findViewById(R.id.field_name_text)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        val view: View

        if (convertView == null) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.layout_settings_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
        }

        val item = items[position]
        // ViewHolderからTextViewを取得し、テキストを設定する
        (view.tag as ViewHolder).textView.text = item.title

        return view
    }
}