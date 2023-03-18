package com.example.tokumemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

// PR画面
class PublicRelationsActivity : AppCompatActivity() {

    // 少量なのでViewModelは作成しない
    lateinit var imageURL: String
    lateinit var introduction: String
    lateinit var description: String
    lateinit var tappedURL: String
//    lateinit var organization_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_public_relations)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val detailedInfoButton = findViewById<Button>(R.id.button)
        detailedInfoButton.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", tappedURL)
            startActivity(intent)
        }

        loadPRData()

        val imageView = findViewById<ImageView>(R.id.pr_image_view)
        GetImage(imageView).execute(imageURL)

        findViewById<TextView>(R.id.textView5).text = introduction
    }

    private fun loadPRData() {
        // HomeFragmentからURLを受け取る
        imageURL = intent.getStringExtra("PR_imageURL").toString()
        introduction = intent.getStringExtra("PR_introduction").toString()
        description = intent.getStringExtra("PR_description").toString()
        tappedURL = intent.getStringExtra("PR_tappedURL").toString()
//        organization_name = intent.getStringExtra("PR_organization_name").toString()
    }

}
