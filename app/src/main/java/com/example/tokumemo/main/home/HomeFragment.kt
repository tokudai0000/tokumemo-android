package com.example.tokumemo.main.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.GetImage
import com.example.tokumemo.R
import com.example.tokumemo.web.WebActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.FileNotFoundException
import java.net.URL
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        viewModel.getPRItems()
        listViewInitSetting(view)
        adImagesRotationTimerON(view)


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

    /// 広告を一定時間ごとに読み込ませる処理のスイッチ
    private fun adImagesRotationTimerON(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.pr_image_button)

        // ImageViewにクリックリスナーを設定
        imageView.setOnClickListener {
            val childFragment = PRFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.child_fragment_container, childFragment).commit()
        }

        // 5000 ms 毎に実行
        Timer().scheduleAtFixedRate(0, 5000) {
            val num = viewModel.selectPRImageNumber()
            if (num != null) {
                val imageTask: GetImage = GetImage(imageView)
                imageTask.execute(viewModel.prItems[num].imageURL)
            }
        }
    }


}