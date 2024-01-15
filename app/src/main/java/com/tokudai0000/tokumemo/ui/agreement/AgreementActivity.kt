package com.tokudai0000.tokumemo.ui.agreement

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.data.repository.AcceptedTermVersionRepository
import com.tokudai0000.tokumemo.ui.RootActivity
import com.tokudai0000.tokumemo.ui.web.WebActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class AgreementActivity : AppCompatActivity(R.layout.activity_agreement) {

    companion object {
        var currentTermVersion: String = ""

        fun createIntent(context: Context) = Intent(context, AgreementActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configureTermText()

        // 利用規約のボタン
        findViewById<Button>(R.id.terms_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", Url.TermsOfService.urlString)
            startActivity(intent)
        }

        // プライバシーポリシーのボタン
        findViewById<Button>(R.id.privacy_button).setOnClickListener {
            val intent = Intent(this, WebActivity::class.java)
            intent.putExtra("PAGE_KEY", Url.PrivacyPolicy.urlString)
            startActivity(intent)
        }

        // 同意するのボタン
        findViewById<Button>(R.id.agreement_button).setOnClickListener {
            AKLog(AKLogLevel.DEBUG, "同意したcurrentTermVersion: $currentTermVersion")

            // 同意した規約のバージョンを保存
            AcceptedTermVersionRepository(this).setAcceptedTermVersion(currentTermVersion)

            // 現状は、RootActivityにAgreementActivityが乗っている状態
            // Agreementを終了させ、RootからMainを呼び出す
            val returnIntent = Intent()
            returnIntent.putExtra(RootActivity.EXTRA_NEXT_ACTIVITY, "MainActivity")
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun configureTermText() {
        val termTextView: TextView = findViewById(R.id.agreement_text_view)

        GlobalScope.launch {
            try {
                termTextView.text = fetchTermText()
            } catch (e: Exception) {
                AKLog(AKLogLevel.ERROR, "Error occurred configureTermText: ${e.message}")
            }
        }
    }

    // FIXME: ViewModel等に移行
    private suspend fun fetchTermText(): String {
        return withContext(Dispatchers.IO) {
            try {
                val apiUrl = "https://tokudai0000.github.io/tokumemo_resource/api/v1/term_text.json"
                val responseData = URL(apiUrl).readText()
                val jsonData = JSONObject(responseData)
                jsonData.getString("termText")
            } catch (e: Exception) {
                "Error: getTermText API通信失敗"
            }
        }
    }
}
