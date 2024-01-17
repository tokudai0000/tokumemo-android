package com.tokudai0000.tokumemo.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    val currentTermVersion = MutableLiveData<String>()

    fun getCurrentTermVersion() {
        viewModelScope.launch {
            try {
                val url = "https://tokudai0000.github.io/tokumemo_resource/api/v1/current_term_version.json"
                url.httpGet().responseJson { _, _, result  ->
                    when (result) {
                        is Result.Success -> {
                            val item: String = result.get().obj().getString("currentTermVersion")
                            currentTermVersion.postValue(item)
                        }
                        is Result.Failure -> {
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getCurrentTermVersion API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getCurrentTermVersion API通信失敗 - 例外: ${exception.message}")
            }
        }
    }
}