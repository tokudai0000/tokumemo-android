package com.example.tokumemo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AgreementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

//        findViewById<TextView>(R.id.agreement_text_view).text =

        findViewById<Button>(R.id.terms_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY",Url.TermsOfService.urlString)
            startActivity(intent)
        }
        findViewById<Button>(R.id.privacy_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY",Url.PrivacyPolicy.urlString)
            startActivity(intent)
        }
        findViewById<Button>(R.id.agreement_button).setOnClickListener {
            val KEY = "KEY_agreementVersion"
            getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
                putString(KEY, DataManager.agreementVer).commit()
            }
            finish()
        }
    }
}