package com.example.tokumemo.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tokumemo.R
import com.example.tokumemo.domain.model.HomeMiniSettingsItem
import com.example.tokumemo.domain.model.SettingsItem

class HomeMiniSettingsRecyclerAdapter(context: Context, val items: List<HomeMiniSettingsItem>) : ArrayAdapter<HomeMiniSettingsItem>(context, R.layout.layout_settings_item, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // ViewHolderパターンを使用してパフォーマンスを向上させる
        val holder: ViewHolder

        // convertViewがnullの場合のみ新しくViewをinflateする
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.layout_settings_item, parent, false).also {
            holder = ViewHolder(it.findViewById(R.id.field_name_text))
            it.tag = holder
        }

        val item = items[position]
        // ViewHolderからTextViewを取得し、テキストを設定する
        (view.tag as ViewHolder).textView.text = item.title

        return view
    }

    // ViewHolderクラスを定義することで、findViewByIdのコールを削減
    private class ViewHolder(val textView: TextView)
}