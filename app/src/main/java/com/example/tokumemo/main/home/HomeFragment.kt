package com.example.tokumemo.main.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R
import com.example.tokumemo.web.WebActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel

    private var resultText = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        listViewInitSetting(view)
        getPRItems()

        return view
    }

    private fun listViewInitSetting(view: View) {

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
    }

//    private fun apiCommunicatingInitSetting(view: View) {
//
//        // 広告画像貼り付け
//        if (encryptedLoad("imageNum") != ""){
//            imageNum = encryptedLoad("imageNum").toInt()
//        }
//        val imageButton = findViewById<ImageButton>(R.id.image)
//
//        getAd()
//
//        imageButton.setOnClickListener{
//            if (adExistence){
//                goWeb(adURL)
//            }
//        }
//    }

    private fun getPRItems(): Job = GlobalScope.launch {
        try {

            val jsonUrl = "https://tokudai0000.github.io/tokumemo_resource/pr_image/info.json"
            val str = URL(jsonUrl).readText()
            val json = JSONObject(str)

            // Jsonデータから内容物を取得
            val itemCounts = json.getInt("itemCounts")
            val items = json.getJSONArray("items")

            for (i in 0..itemCounts-1) {
                var item = items.getJSONObject(i)
                var prItem = HomeViewModel.PublicRelations(
                    imageURL = item.getString("imageURL"),
                    introduction = item.getString("introduction"),
                    tappedURL = item.getString("tappedURL"),
                    organization_name = item.getString("organization_name"),
                    description = item.getString("description")
                )
                viewModel.prItems.add(prItem)
            }

        } catch (e: Exception) {
            // Error
        }
    }
}