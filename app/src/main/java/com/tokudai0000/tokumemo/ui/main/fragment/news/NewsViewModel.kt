package com.tokudai0000.tokumemo.ui.main.fragment.news

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.domain.model.NewsListData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONArray
import com.github.kittinunf.result.*

class NewsViewModel: ViewModel() {

    val newsItems = MutableLiveData<ArrayList<NewsListData>>()

    fun fetchNews() {
        val url = Url.NewsItemJsonData.urlString
        url.httpGet().responseJson { _, _, result ->
            when (result) {
                is Result.Success -> {
                    val items: JSONArray = result.get().obj().getJSONArray("items")
                    val newsList = ArrayList<NewsListData>()

                    for (i in 0 until items.length()) {
                        newsList.add(NewsListData().apply {
                            title = items.getJSONObject(i)["title"].toString()
                            pubDate = items.getJSONObject(i)["pubDate"].toString()
                            link = items.getJSONObject(i)["link"].toString()
                        })
                    }
                    newsItems.postValue(newsList)
                }
                is Result.Failure -> {
                    // エラーハンドリング
                    val error = result.getException()
                    Log.e("NewsViewModel", "Error fetching news: ${error.message}", error)
                }
                else -> {
                    // 予期しない結果を扱う
                    Log.wtf("NewsViewModel", "Unexpected result type encountered")
                }
            }
        }
    }
}