package com.example.tokumemo.ui.agreement

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.R
import com.example.tokumemo.common.Url
import com.example.tokumemo.ui.splash.SplashActivity
import com.example.tokumemo.ui.web.WebActivity

class AgreementActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_RESULT = "result"

        fun createIntent(context: Context) =
            Intent(context, AgreementActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        findViewById<Button>(R.id.terms_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", Url.TermsOfService.urlString)
            startActivity(intent)
        }
        findViewById<Button>(R.id.privacy_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", Url.PrivacyPolicy.urlString)
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