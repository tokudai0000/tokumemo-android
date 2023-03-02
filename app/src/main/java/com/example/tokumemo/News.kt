package com.example.tokumemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.*
import org.json.JSONArray
import org.json.JSONObject

class News : Fragment() {

    private lateinit var titleArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_news, container, false)

        // 配列の生成
//        titleArray = arrayOf("リスト１", "リスト２", "リスト３", "リスト４", "リスト５",)

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.list_view)

        // ArrayAdapterの生成
//        val adapter = ArrayAdapter<String>(requireContext(), R.layout.item_layout, titleArray)

        // ListViewに、生成したAdapterを設定
//        listView.adapter = adapter

        Log.d("PRINT", "Applemode中はオレンジボタンは無効1")

        /// リクエストURL
        val url = "https://api.rss2json.com/v1/api.json?rss_url=https://www.tokushima-u.ac.jp/recent/rss.xml"

        url.httpGet().responseJson { request, response, result ->
            when (result) {
                is Result.Success -> {
                    Log.d("PRINT", "Success")
                    titleArray = arrayOf("")
                    val items:JSONArray = result.get().obj().getJSONArray("items")


                    for(i in 0..items.length() - 1 )
                        if (i == 0) {
                            titleArray[0] = items.getJSONObject(0)["title"].toString()
                        }else{
                            titleArray += items.getJSONObject(i)["title"].toString()
                        }
//                        titleArray.add()
//                    Log.d("PRINT", result.get().obj().toString())
//                    Log.d("PRINT", result.get().obj().get("items").toString())
//                    Log.d("PRINT", items.getJSONObject(i)["title"].toString())
                }
                is Result.Failure -> {
                    val ex = result.getException()

                    JSONObject(mapOf("message" to ex.toString()))
                    Log.d("PRINT", request.toString())
                    Log.d("PRINT", response.toString())
                    Log.d("PRINT", result.toString())
                }
            }
            Log.d("PRINT", "Fin")
            listView.adapter = ArrayAdapter<String>(requireContext(), R.layout.item_layout, titleArray)
        }

        return view
    }

}

