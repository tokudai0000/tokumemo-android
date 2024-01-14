package com.tokudai0000.tokumemo.ui.agreement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.data.DataManager
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.ui.RootActivity
import com.tokudai0000.tokumemo.ui.web.WebActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class AgreementActivity : AppCompatActivity() {

    companion object {
        var agreementVer = ""
        const val EXTRA_RESULT = "result"

        fun createIntent(context: Context) =
            Intent(context, AgreementActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        val termText: TextView = findViewById(R.id.agreement_text_view)

        GlobalScope.launch {
            try {
                termText.text = getTermText()
            } catch (e: Exception) {
                println("Error occurred: ${e.message}")
            }
        }

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

            AKLog(AKLogLevel.DEBUG, agreementVer)
            getSharedPreferences("my_settings", Context.MODE_PRIVATE).edit().apply {
                putString(KEY, agreementVer).commit()
            }

            val returnIntent = Intent()
            returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private suspend fun getTermText(): String {
        val apiUrl = "https://tokudai0000.github.io/tokumemo_resource/api/v1/term_text.json"

        return withContext(Dispatchers.IO) {
            try {
                val responseData = URL(apiUrl).readText()
                val jsonData = JSONObject(responseData)

                // Jsonデータから内容物を取得
                jsonData.getString("termText")
            } catch (e: Exception) {
                // エラー処理、適宜エラーメッセージを返す
                "Error: ${e.message}"
            }
        }
    }
}