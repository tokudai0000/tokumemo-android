package com.example.tokumemo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MenuListsAdapter(private val fields: List<MenuData>): RecyclerView.Adapter<MenuListsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val fieldImage: ImageView = view.findViewById(R.id.field_image)
        val fieldName: TextView = view.findViewById(R.id.field_name_text)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.menu_recycle_item, viewGroup, false)
        return ViewHolder(view)
    }

    // 1. リスナを格納する変数を定義（lateinitで初期化を遅らせている）
    private lateinit var listener: OnBookCellClickListener

    // 2. インターフェースを作成
    interface  OnBookCellClickListener {
        fun onItemClick(book: MenuData)
    }

    // 3. リスナーをセット
    fun setOnBookCellClickListener(listener: OnBookCellClickListener) {
        // 定義した変数listenerに実行したい処理を引数で渡す（BookListFragmentで渡している）
        this.listener = listener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val field = fields[position]
        holder.fieldName.text = field.title
        holder.fieldImage.setImageResource(field.image)

        // 4. セルのクリックイベントにリスナをセット
        holder.itemView.setOnClickListener {
            // セルがクリックされた時にインターフェースの処理が実行される
            listener.onItemClick(field)
        }
    }

    override fun getItemCount(): Int {
        return fields.size
    }

}