package com.example.tokumemo.ui.pr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tokumemo.utility.GetImage
import com.example.tokumemo.R
import com.example.tokumemo.domain.model.AdItem
import com.example.tokumemo.ui.web.WebActivity

// PR画面
class PublicRelationsActivity : AppCompatActivity() {

    lateinit var item: AdItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item = intent.getSerializableExtra("PAGE_KEY") as AdItem

        setContentView(R.layout.activity_public_relations)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val detailedInfoButton = findViewById<Button>(R.id.button)
        detailedInfoButton.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", item.targetUrlStr)
            startActivity(intent)
        }

        val imageView = findViewById<ImageView>(R.id.pr_image_view)
        GetImage(imageView).execute(item.imageUrlStr)

        findViewById<TextView>(R.id.textView5).text = item.imageDescription
    }
}
