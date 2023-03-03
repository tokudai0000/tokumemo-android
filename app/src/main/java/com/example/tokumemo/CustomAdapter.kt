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

//    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//    override fun getCount(): Int {
//        return data.size
//    }
//
//    override fun getItem(p0: Int): Data {
//        return data[p0]
//    }
//
////    override fun getItemId(p0: Int): Long {
////        return data[p0]
////    }
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var view  = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
//        val item = getItem(position)
////        if (view == null) {
////            // 一行分のレイアウトを生成
////            view  = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
////        } else {
////            view.findViewById<TextView>(R.id.item_title).text = item.title
////        }
//        view.findViewById<TextView>(R.id.item_title).text = item.title
//
////        val item = getItem(p0)
////        val view = p1 ?: inflater.inflate(resource, null)
////        view.findViewById<TextView>(R.id.text_title).text = item.title
////        view.findViewById<TextView>(R.id.text_id).text = item.id.toString()
//        return view!!
//    }


//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        var view  = convertView
//        if (view == null) {
//            // 一行分のレイアウトを生成
//            view  = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false)
//        } else {
//            view.findViewById<TextView>(R.id.item_title).text =
//        }
//        // 一行分のデータを取得
////        val data = getItem(position)
////        view?.findViewById<TextView>(R.id.item_title).text = "aa"
////        view?.findViewById<TextView>(R.id.item_text).apply { data?.text }
//        return view!!
//    }
}