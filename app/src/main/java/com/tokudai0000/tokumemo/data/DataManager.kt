package com.tokudai0000.tokumemo.data

public final class DataManager {
    companion object {

        public var agreementVer = ""

        data class LoginState(
            var isProgress: Boolean = false,          // 進行中
            var completeImmediately: Boolean = false, // 完了してすぐ
            var completed: Boolean = false,           // ログイン完了
        )
        public var loginState: LoginState = LoginState()

        /// JavaScriptを動かすかどうかのフラグ
        ///
        /// 次に読み込まれるURLはJavaScriptを動かすことを許可する
        /// これがないと、ログインに失敗した場合、永遠とログイン処理を行われてしまう
        public var canExecuteJavascript = true
        var jsCount = 0
    }

}