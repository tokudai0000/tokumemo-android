package com.example.tokumemo.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R

class MenuListsAdapter(private val fields: List<MenuData>): RecyclerView.Adapter<MenuListsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val fieldImage: ImageView = view.findViewById(R.id.field_image)
        val fieldName: TextView = view.findViewById(R.id.field_name_text)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.menu_recycle_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = fields[position]
        holder.fieldName.text = field.title
//        holder.fieldPrefecture.text = field.
    }

    override fun getItemCount(): Int {
        return fields.size
    }
}