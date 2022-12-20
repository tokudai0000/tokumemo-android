package com.example.tokumemo.manager

import android.widget.ImageButton
import com.example.tokumemo.GetImage
import com.example.tokumemo.R
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL

public final class DataManager {
    companion object {
        /// JavaScriptを動かすかどうかのフラグ
        ///
        /// 次に読み込まれるURLはJavaScriptを動かすことを許可する
        /// これがないと、ログインに失敗した場合、永遠とログイン処理を行われてしまう
        public var canExecuteJavascript = true
        var jsCount = 0
    }
}