package com.tokudai0000.tokumemo.ui.pr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.utility.GetImage
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.domain.model.AdItem
import com.tokudai0000.tokumemo.ui.web.WebActivity

// PR画面
class PublicRelationsActivity : AppCompatActivity() {

    lateinit var item: AdItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        item = intent.getSerializableExtra(WebActivity.KEY_URL) as AdItem

        setContentView(R.layout.activity_public_relations)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        val detailedInfoButton = findViewById<Button>(R.id.button)
        detailedInfoButton.setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra(WebActivity.KEY_URL, item.targetUrlStr)
            startActivity(intent)
        }

        val imageView = findViewById<ImageView>(R.id.pr_image_view)
        GetImage(imageView).execute(item.imageUrlStr)

        findViewById<TextView>(R.id.textView5).text = item.imageDescription
    }
}
