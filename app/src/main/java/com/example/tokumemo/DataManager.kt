package com.example.tokumemo

import android.content.Context
import decrypt
import encrypt
import java.util.*

public final class DataManager {
    companion object {

        public var cAccount = {

        }

        /// JavaScriptを動かすかどうかのフラグ
        ///
        /// 次に読み込まれるURLはJavaScriptを動かすことを許可する
        /// これがないと、ログインに失敗した場合、永遠とログイン処理を行われてしまう
        public var canExecuteJavascript = true
        var jsCount = 0
    }

}