package com.tokudai0000.tokumemo.ui.main.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.domain.model.AdItem
import com.tokudai0000.tokumemo.domain.model.HomeEventInfoButtonItems
import com.tokudai0000.tokumemo.domain.model.HomeEventInfoPopupItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL

class HomeViewModel: ViewModel() {

    val numberOfUsers = MutableLiveData<String>()

    var prItems = arrayListOf<AdItem>()
    var displayPrItem = MutableLiveData<AdItem>()

    var univItems = arrayListOf<AdItem>()
    var displayUnivItem = MutableLiveData<AdItem>()

    var popupItems = MutableLiveData<ArrayList<HomeEventInfoPopupItems>>(arrayListOf())
    var buttonItems = MutableLiveData<ArrayList<HomeEventInfoButtonItems>>(arrayListOf())

    val libraryCalendarURL = MutableLiveData<String>()

    fun getNumberOfUsers() {
        viewModelScope.launch {
            try {
                val url = "https://tokudai0000.github.io/tokumemo_resource/api/v1/number_of_users.json"
                url.httpGet().responseJson { _, _, result  ->
                    when (result) {
                        is Result.Success -> {
                            val item: String = result.get().obj().getString("numberOfUsers")
                            numberOfUsers.postValue(item)
                        }
                        is Result.Failure -> {
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getNumberOfUsers API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getCurrentTermVersion API通信失敗 - 例外: ${exception.message}")
            }
        }
    }

    fun getAdItems() {
        viewModelScope.launch {
            try {
                val url = "https://tokudai0000.github.io/tokumemo_resource/api/v1/ad_items.json"
                url.httpGet().responseJson { _, _, result  ->
                    when (result) {
                        is Result.Success -> {

                            // Jsonデータから内容物を取得
                            val prItems = result.get().obj().getJSONArray("prItems")
                            val univItems = result.get().obj().getJSONArray("univItems")

                            for (i in 0 until prItems.length()) {
                                prItems.getJSONObject(i)?.let {
                                    val jsonObject = prItems.getJSONObject(i)
                                    val prItem = AdItem(
                                        id = jsonObject.getInt("id"),
                                        clientName = jsonObject.getString("clientName"),
                                        imageUrlStr = jsonObject.getString("imageUrlStr"),
                                        targetUrlStr = jsonObject.getString("targetUrlStr"),
                                        imageDescription = jsonObject.getString("imageDescription"),
                                    )
                                    displayPrItem.postValue(prItem)
                                    this@HomeViewModel.prItems.add(prItem)
                                }
                            }

                            for (j in 0 until univItems.length()) {
                                univItems.getJSONObject(j)?.let {
                                    AKLog(AKLogLevel.DEBUG, "univItems $it")
                                    val jsonObject = univItems.getJSONObject(j)
                                    val univItem = AdItem(
                                        id = jsonObject.getInt("id"),
                                        clientName = jsonObject.getString("clientName"),
                                        imageUrlStr = jsonObject.getString("imageUrlStr"),
                                        targetUrlStr = jsonObject.getString("targetUrlStr"),
                                        imageDescription = jsonObject.getString("imageDescription"),
                                    )
                                    displayUnivItem.postValue(univItem)
                                    this@HomeViewModel.univItems.add(univItem)
                                }
                            }
                        }
                        is Result.Failure -> {
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getAdItems API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getAdItems API通信失敗 - 例外: ${exception.message}")
            }
        }
    }

    fun getHomeEventInfos() {
        viewModelScope.launch {
            try {
                val url = "https://tokudai0000.github.io/tokumemo_resource/api/stub/home_event_infos.json"
//                val url = "https://tokudai0000.github.io/tokumemo_resource/api/v1/home_event_infos.json"
                url.httpGet().responseJson { _, _, result  ->
                    when (result) {
                        is Result.Success -> {

                            // Jsonデータから内容物を取得
//                            val popupItems = result.get().obj().getJSONArray("popupItems")
                            val buttonItems = result.get().obj().getJSONArray("buttonItems")

//                            for (i in 0 until popupItems.length()) {
//                                popupItems.getJSONObject(i)?.let {
//                                    val jsonObject = popupItems.getJSONObject(i)
//                                    val popupItems = HomeEventInfoPopupItems(
//                                        id = jsonObject.getInt("id"),
//                                        clientName = jsonObject.getString("clientName"),
//                                        titleName = jsonObject.getString("titleName"),
//                                        description = jsonObject.getString("targetUrlStr"),
//                                    )
//                                    this@HomeViewModel.popupItems.value?.add(popupItems)
//                                }
//                            }

                            for (j in 0 until buttonItems.length()) {
                                buttonItems.getJSONObject(j)?.let {
                                    AKLog(AKLogLevel.DEBUG, "buttonItems $it")
                                    val jsonObject = buttonItems.getJSONObject(j)
                                    val buttonItems = HomeEventInfoButtonItems(
                                        id = jsonObject.getInt("id"),
                                        clientName = jsonObject.getString("clientName"),
                                        titleName = jsonObject.getString("titleName"),
                                        targetUrlStr = jsonObject.getString("targetUrlStr"),
                                    )
                                    this@HomeViewModel.buttonItems.value?.add(buttonItems)
                                }
                            }

                            this@HomeViewModel.buttonItems.postValue(this@HomeViewModel.buttonItems.value)
                        }
                        is Result.Failure -> {
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getHomeEventInfos API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getHomeEventInfos API通信失敗 - 例外: ${exception.message}")
            }
        }
    }
    
    fun randomChoiceForAdImage(adItems: ArrayList<AdItem>, displayAdItem: AdItem?): AdItem? {
        // 広告数が0か1の場合はローテーションする必要がない
        if (adItems.count() == 0) {
            return null
        }

        if (adItems.count() == 1) {
            return adItems[0]
        }

        displayAdItem?.let {
            // 必ず2つ以上の広告が存在することを確認済み
            while (true) {
                val ramdomItem = adItems.random()
                // 前回の画像表示番号と同じであれば、再度繰り返す
                if (ramdomItem != displayAdItem) {
                    return ramdomItem
                }
            }
        }
        return adItems.random()
    }

    fun getLibraryCalendarURL(baseURLString: String) {
        viewModelScope.launch {
            try {
                val libraryUrl = URL(baseURLString)

                withContext(Dispatchers.IO) {
                    val connection = libraryUrl.openConnection() as HttpURLConnection
                    connection.requestMethod = "GET"

                    val responseCode = connection.responseCode
                    if (responseCode != HttpURLConnection.HTTP_OK) {
                        AKLog(AKLogLevel.ERROR, "Error: getLibraryCalendarURL レスポンスコードが正しくありません - $responseCode")
                        return@withContext
                    }

                    val inputStream = connection.inputStream
                    val data = inputStream.bufferedReader().use { it.readText() }

                    var urlStr = "https://www.lib.tokushima-u.ac.jp/"
                    try {
                        val doc = Jsoup.parse(data)
                        val elements = doc.select("a[href*=\"pub/pdf/calender/\"]")
                        for (element in elements) {
                            val str = element.attr("href")
                            urlStr += str
                        }
                    } catch (parseError: Throwable) {
                        AKLog(AKLogLevel.ERROR, "Error: getLibraryCalendarURL HTMLの解析に失敗しました - 例外: ${parseError.message}")
                        return@withContext
                    }
                    AKLog(AKLogLevel.DEBUG, "Success: Library calendar URL 取得成功 - $urlStr")
                    libraryCalendarURL.postValue(urlStr)
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getLibraryCalendarURL 通信失敗 - 例外: ${exception.message}")
            }
        }
    }
}