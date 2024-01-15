package com.tokudai0000.tokumemo.ui.clublists

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JsResult
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.ui.web.WebActivity

class ClubListsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_club_lists, container, false)
        val webView = view.findViewById<WebView>(R.id.club_lists_web_view)

        webView.settings.javaScriptEnabled = true

        webView.loadUrl(Url.ClubLists.urlString)
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