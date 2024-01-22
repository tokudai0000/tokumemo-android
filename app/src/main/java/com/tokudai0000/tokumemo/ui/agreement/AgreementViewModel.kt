package com.tokudai0000.tokumemo.ui.agreement

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import kotlinx.coroutines.launch

class AgreementViewModel: ViewModel() {

    val termText = MutableLiveData<String>()

    fun fetchTermText() {
        viewModelScope.launch {
            try {
                val url = "https://tokudai0000.github.io/tokumemo_resource/api/v1/term_text.json"
                url.httpGet().responseJson { _, _, result  ->
                    when (result) {
                        is Result.Success -> {
                            val item: String = result.get().obj().getString("termText")
                            termText.postValue(item)
                        }
                        is Result.Failure -> {
                            val error = result.getException()
                            AKLog(AKLogLevel.ERROR, "Error: getTermText API通信失敗 - ステータスコード: ${error.message}")
                        }
                    }
                }
            } catch (exception: Exception) {
                AKLog(AKLogLevel.ERROR, "Error: getTermText API通信失敗 - 例外: ${exception.message}")
            }
        }
    }
}