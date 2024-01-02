package com.example.tokumemo.ui.clublists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.tokumemo.R
import com.example.tokumemo.common.Url
import com.example.tokumemo.ui.web.WebActivity

class ClubListsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_club_lists, container, false)
        val webView = view.findViewById<WebView>(R.id.club_lists_web_view)
        // 有効にすることでHTML側からネイティブ側へアクションを起こしている
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(Url.ClubList.urlString)

        webView.webChromeClient = object : WebChromeClient() {
            // Web側(HTML,JavaScript)からKotlinが通知を受け、WebActivityを表示させる
            override fun onJsAlert(view: WebView, url: String, message: String, result: JsResult): Boolean {
                // アラートの表示をキャンセルする
                result.cancel()

                val intent = Intent(requireContext(), WebActivity::class.java)
                intent.putExtra("PAGE_KEY",message)
                startActivity(intent)

                return true
            }
        }
        return view
    }
}