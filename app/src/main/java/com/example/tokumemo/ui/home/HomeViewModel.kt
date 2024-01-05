package com.example.tokumemo.ui.home

import androidx.lifecycle.ViewModel
import com.example.tokumemo.domain.model.AdItem
import com.example.tokumemo.domain.model.PublicRelationsData
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class HomeViewModel: ViewModel() {

    var prItems = arrayListOf<PublicRelationsData>()
    var displayPrItem: AdItem? = null

    var displayPRImagesNumber: Int? = null // 表示している広告がadItemsに入っている配列番号

    fun getAdItems(): Job = GlobalScope.launch {
        try {
            val jsonUrl = "https://tokudai0000.github.io/tokumemo_resource/pr_image/info.json"
            val str = URL(jsonUrl).readText()
            val json = JSONObject(str)

            // Jsonデータから内容物を取得
            val itemCounts = json.getInt("itemCounts")
            val items = json.getJSONArray("items")

            for (i in 0 until itemCounts) {
                var item = items.getJSONObject(i)
                var prItem = PublicRelationsData(
                    imageURL = item.getString("imageURL"),
                    introduction = item.getString("introduction"),
                    tappedURL = item.getString("tappedURL"),
                    organization_name = item.getString("organization_name"),
                    description = item.getString("description")
                )
                prItems.add(prItem)
            }

        } catch (e: Exception) {
            // Error
        }
    }
    
    fun selectPRImageNumber(): Int? {
        // 広告数が0か1の場合はローテーションする必要がない
        if (prItems.count() == 0) {
            return null
        } else if (prItems.count() == 1) {
            return 0
        }

        while (true) {
            val randomNum = kotlin.random.Random.nextInt(0, prItems.count())
            // 前回の画像表示番号と同じであれば、再度繰り返す
            if (randomNum != displayPRImagesNumber) {
                return randomNum
            }
        }

    }
}