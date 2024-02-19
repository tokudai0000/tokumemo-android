package com.tokudai0000.tokumemo.data.repository

import android.content.Context

interface AcceptedTermVersionRepositoryInterface {
    fun fetchAcceptedTermVersion(): String
    fun setAcceptedTermVersion(items: String)
}


class AcceptedTermVersionRepository(private val context: Context) : AcceptedTermVersionRepositoryInterface {

    private val key = "KEY_agreementVersion"
    private val saveFileName = "my_settings"

    override fun fetchAcceptedTermVersion(): String {
        val sharedPreferences = context.getSharedPreferences(saveFileName, Context.MODE_PRIVATE)
        return sharedPreferences.getString(key, "") ?: ""
    }

    override fun setAcceptedTermVersion(items: String) {
        context.getSharedPreferences(saveFileName, Context.MODE_PRIVATE).edit().apply {
            putString(key, items).apply()
        }
    }
}
