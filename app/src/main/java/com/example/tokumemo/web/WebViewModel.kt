package com.example.tokumemo.web

import androidx.lifecycle.ViewModel
import com.example.tokumemo.model.DataManager
import com.example.tokumemo.model.Url

class WebViewModel: ViewModel() {
    // Safariで開く用として、現在表示しているURLを保存する
    public var loadingUrlStr = "https://www.google.com/?hl=ja"

//    private val dataManager = DataManager.singleton

//    private val safariUrls = ["https://teams.microsoft.com/l/meetup-join", "https://zoom.us/meeting/register/","https://join.skype.com/"]

    /// タイムアウトのURLであるか判定
    public fun isTimeout(urlStr: String): Boolean {
        return urlStr == Url.universityServiceTimeOut.urlString || urlStr == Url.universityServiceTimeOut2.urlString
    }

    /// Safariで開きたいURLであるか判定
//    public func shouldOpenSafari(urlStr: String) -> Bool {
//        for url in safariUrls {
//            if urlStr.contains(url) {
//                return true
//            }
//        }
//        return false
//    }

    /// JavaScriptを動かす種類
    enum class JavaScriptType {
        skipReminder, // アンケート解答の催促画面
        syllabus, // シラバスの検索画面
        loginIAS, // 大学統合認証システム(IAS)のログイン画面
        loginOutlook, // メール(Outlook)のログイン画面
        loginCareerCenter, // 徳島大学キャリアセンターのログイン画面
        none // JavaScriptを動かす必要がない場合
    }
    /// JavaScriptを動かしたい指定のURLかどうかを判定し、動かすJavaScriptの種類を返す
    ///
    /// - Note: canExecuteJavascriptが重要な理由
    ///   ログインに失敗した場合に再度ログインのURLが表示されることになる。
    ///   canExecuteJavascriptが存在しないと、再度ログインの為にJavaScriptが実行され続け無限ループとなってしまう。
    public fun anyJavaScriptExecute(urlString: String): JavaScriptType {
        // JavaScriptを実行するフラグが立っていない場合はnoneを返す
        if (DataManager.canExecuteJavascript == false) {
            return JavaScriptType.none
        }
        // アンケート解答の催促画面
        if (urlString == Url.skipReminder.urlString) {
            return JavaScriptType.skipReminder
        }
        // 大学統合認証システム(IAS)のログイン画面
        if (urlString in Url.universityLogin.urlString) {
            return JavaScriptType.loginIAS
        }
        // シラバスの検索画面
        if (urlString == Url.syllabus.urlString) {
            return JavaScriptType.syllabus
        }
        // メール(Outlook)のログイン画面
        if (urlString in Url.outlookLoginForm.urlString) {
            return JavaScriptType.loginOutlook
        }
        // 徳島大学キャリアセンターのログイン画面
        if (urlString == Url.tokudaiCareerCenter.urlString) {
            return JavaScriptType.loginCareerCenter
        }
        // それ以外なら
        return JavaScriptType.none
    }
}