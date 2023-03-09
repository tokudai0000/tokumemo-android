package com.example.tokumemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient

class ClubListsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_club_lists, container, false)
        val webView = view.findViewById<WebView>(R.id.club_lists_webview)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(Url.ClubList.urlString)

        return view
    }

}