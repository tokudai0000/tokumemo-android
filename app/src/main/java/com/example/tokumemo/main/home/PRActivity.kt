package com.example.tokumemo.main.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.tokumemo.GetImage
import com.example.tokumemo.R
import com.example.tokumemo.web.WebActivity

class PRActivity : AppCompatActivity() {

//    lateinit var imageStr: HomeViewModel.PublicRelations

    lateinit var imageURL: String
    lateinit var introduction: String
    lateinit var description: String
    lateinit var tappedURL: String
    lateinit var organization_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pr)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
            intent.putExtra("PAGE_KEY",tappedURL)
            startActivity(intent)
        }

        loadPRData()

        val imageView = findViewById<ImageView>(R.id.pr_image_view)
        val imageTask: GetImage = GetImage(imageView)
        imageTask.execute(imageURL)

        var text = findViewById<TextView>(R.id.textView5)
        text.text = introduction
    }

    fun loadPRData() {
        // HomeFragemntからURLを受け取る
        imageURL = intent.getStringExtra("PR_imageURL").toString()
        introduction = intent.getStringExtra("PR_introduction").toString()
        description = intent.getStringExtra("PR_description").toString()
        tappedURL = intent.getStringExtra("PR_tappedURL").toString()
        organization_name = intent.getStringExtra("PR_organization_name").toString()
    }
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//        val view = inflater.inflate(R.layout.activity_pr, container, false)
//
//        val backButton = view.findViewById<ImageButton>(R.id.back_button)
//        backButton.setOnClickListener {
//            // 子フラグメントマネージャーを取得
//            val childFragmentManager = parentFragment?.childFragmentManager
//
//            // 子フラグメントを終了する
//            if (childFragmentManager != null) {
//                val childFragment = childFragmentManager.findFragmentById(R.id.child_fragment_container)
//                if (childFragment != null) {
//                    childFragmentManager.beginTransaction()
//                        .remove(childFragment)
//                        .commit()
//                }
//            }
//        }
//
//        val imageView = view.findViewById<ImageView>(R.id.pr_image_view)
//        val imageTask: GetImage = GetImage(imageView)
//        imageTask.execute(imageStr.imageURL)
//
//        var text = view.findViewById<TextView>(R.id.textView5)
//        text.text = imageStr.introduction
//
//
//        return view
//
//    }
//
//    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
//        return true // 下のフラグメントのタップを無効化するために true を返す
//    }
}
