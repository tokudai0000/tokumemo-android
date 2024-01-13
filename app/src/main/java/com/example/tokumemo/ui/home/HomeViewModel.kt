package com.example.tokumemo.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.tokumemo.domain.model.AdItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class HomeViewModel: ViewModel() {

    var prItems = arrayListOf<AdItem>()
    var displayPrItem: AdItem? = null

    var univItems = arrayListOf<AdItem>()
    var displayUnivItem: AdItem? = null


    fun getAdItems(): Job = GlobalScope.launch {
        try {
            val jsonUrl = "https://tokudai0000.github.io/tokumemo_resource/api/v1/ad_items.json"
            val str = URL(jsonUrl).readText()
//            print("😊"+jsonUrl)
            print("😊"+str)
            val json = JSONObject(str)
            print("😊"+json)

            // Jsonデータから内容物を取得
            val prItems = json.getJSONArray("prItems")
            val univItems = json.getJSONArray("univItems")

            for (i in 0 until prItems.length()) {
                val jsonObject = prItems.getJSONObject(i)
                val prItem = AdItem(
                    id = jsonObject.getInt("id"),
                    clientName = jsonObject.getString("clientName"),
                    imageUrlStr = jsonObject.getString("imageUrlStr"),
                    targetUrlStr = jsonObject.getString("targetUrlStr"),
                    imageDescription = jsonObject.getString("imageDescription"),
                )
                this@HomeViewModel.prItems.add(prItem)
            }

            for (i in 0 until univItems.length()) {
                val jsonObject = univItems.getJSONObject(i)
                val prItem = AdItem(
                    id = jsonObject.getInt("id"),
                    clientName = jsonObject.getString("clientName"),
                    imageUrlStr = jsonObject.getString("imageUrlStr"),
                    targetUrlStr = jsonObject.getString("targetUrlStr"),
                    imageDescription = jsonObject.getString("imageDescription"),
                )
                this@HomeViewModel.univItems.add(prItem)
            }

        } catch (e: Exception) {
            // Error
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
}