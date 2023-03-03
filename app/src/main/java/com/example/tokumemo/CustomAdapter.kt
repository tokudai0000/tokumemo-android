package com.example.tokumemo

import android.content.Context
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CustomAdapter(context: Context, val items: ArrayList<Data>) : ArrayAdapter<Data>(context, R.layout.item_layout, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
        }

        val item = items[position]
        val textView = view?.findViewById<TextView>(R.id.item_title)
        textView?.text = item.title

        return view!!
    }
    
}