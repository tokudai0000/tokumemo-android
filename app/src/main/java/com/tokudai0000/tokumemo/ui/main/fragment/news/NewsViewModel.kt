package com.tokudai0000.tokumemo.ui.main.fragment.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokudai0000.tokumemo.common.Url
import com.tokudai0000.tokumemo.domain.model.NewsListData
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import org.json.JSONArray
import com.github.kittinunf.result.*
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import kotlinx.coroutines.launch

class NewsViewModel: ViewModel() {

    val newsItems = MutableLiveData<ArrayList<NewsListData>>()

    fun getNewsRSS() {
        viewModelScope.launch {
            try {
                val url = Url.NewsItemJsonData.urlString
                url.httpGet().responseJson { _, _, result  ->
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
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getTermText API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getNewsRSS API通信失敗 - 例外: ${exception.message}")
            }
        }
    }
}