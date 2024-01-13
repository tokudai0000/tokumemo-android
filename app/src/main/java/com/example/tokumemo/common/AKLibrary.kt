package com.example.tokumemo.common

import com.example.tokumemo.BuildConfig

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
        val stackTraceElement = throwable.stackTrace[0]
        val fileName = stackTraceElement.fileName
        val lineNumber = stackTraceElement.lineNumber
        val methodName = stackTraceElement.methodName

        val levelString = when (level) {
            AKLogLevel.DEBUG -> "DEBUG"
            AKLogLevel.WARN -> "WARN"
            AKLogLevel.ERROR -> "ERROR"
            AKLogLevel.FATAL -> "FATAL"
        }

        val datetime = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", java.util.Locale.getDefault()).format(java.util.Date())

        println("AKLog: $datetime $levelString $fileName($lineNumber) $methodName: $message")
    }
}
