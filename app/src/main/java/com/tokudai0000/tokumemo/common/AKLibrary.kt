package com.tokudai0000.tokumemo.common

import com.tokudai0000.tokumemo.BuildConfig

enum class AKLogLevel {
    DEBUG, WARN, ERROR, FATAL
}

fun AKLog(
    level: AKLogLevel,
    message: Any,
    throwable: Throwable = Throwable()
) {
    // デバッグビルドのみでログを出力する
    if (BuildConfig.DEBUG) {

        val levelString = when (level) {
            AKLogLevel.DEBUG -> "DEBUG"
            AKLogLevel.WARN -> "WARN"
            AKLogLevel.ERROR -> "ERROR"
            AKLogLevel.FATAL -> "FATAL"
        }

        val datetime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.getDefault()).format(java.util.Date())

        println("AKLog: $datetime $levelString $message")
    }
}
