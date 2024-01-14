package com.tokudai0000.tokumemo.data

class DataManager {
    companion object {

        /// JavaScriptを動かすかどうかのフラグ
        ///
        /// 次に読み込まれるURLはJavaScriptを動かすことを許可する
        /// これがないと、ログインに失敗した場合、永遠とログイン処理を行われてしまう
        var canExecuteJavascript = true
        var jsCount = 0
    }

}