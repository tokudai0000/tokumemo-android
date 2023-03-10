package com.example.tokumemo

import androidx.lifecycle.ViewModel

class MainModel: ViewModel() {

//    /// 最新の利用規約同意者か判定し、同意画面の表示を行うべきか判定
//    public fun shouldShowTermsAgreementView(): Boolean {
//        return (DataManager.agreementVersion != ConstStruct.latestTermsVersion)
//    }
//
//    // 学生番号、パスワードを登録しているか判定
//    public fun hasRegisteredPassword(): Boolean {
//        return !(DataManager.cAccount.isEmpty || DataManager.password.isEmpty)
//    }
//
//    /// タイムアウトのURLであるか判定
//    public fun isTimeout(urlStr: String): Boolean {
//        return urlStr == Url.UniversityServiceTimeOut.urlString || urlStr == Url.UniversityServiceTimeOut2.urlString
//    }
//
//    /// JavaScriptを動かしたい指定のURLか判定
//    public fun canExecuteJS(urlString: String): Boolean {
//        // JavaScriptを実行するフラグ
//        if (DataManager.canExecuteJavascript == false) { return false }
//
//        // cアカウント、パスワードの登録確認
//        if (hasRegisteredPassword() == false) { return false }
//
//        // 大学統合認証システム(IAS)のログイン画面の判定
//        if (urlString.contains(Url.UniversityLogin.toString())) { return true }
//
//        // それ以外なら
//        return false
//    }






















//    /// ログイン処理中かどうか
//    var isLoginProcessing = false
//
//    /// JavaScriptを動かす種類
//    enum class JavaScriptType {
//        loginIAS, // 大学統合認証システム(IAS)のログイン画面
//        none, // JavaScriptを動かす必要がない場合
//    }
//    /// JavaScriptを動かしたい指定のURLかどうかを判定し、動かすJavaScriptの種類を返す
//    ///
//    /// - Note: canExecuteJavascriptが重要な理由
//    ///   ログインに失敗した場合に再度ログインのURLが表示されることになる。
//    ///   canExecuteJavascriptが存在しないと、再度ログインの為にJavaScriptが実行され続け無限ループとなってしまう。
//    /// - Parameter urlString: 読み込み完了したURLの文字列
//    /// - Returns: 動かすJavaScriptの種類
//
//    fun anyJavaScriptExecute(urlString: String): JavaScriptType {
//        // 大学統合認証システム(IAS)のログイン画面
//        if (urlString.startsWith("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=", 0)) {
//            DataManager.canExecuteJavascript = true
//            return JavaScriptType.loginIAS
//        }
//        // JavaScriptを実行するフラグが立っていない場合はnoneを返す
//        if (DataManager.canExecuteJavascript == false) {
//            return JavaScriptType.none
//        }
//        // それ以外なら
//        return JavaScriptType.none
//    }
//
//    /// 大学統合認証システム(IAS)へのログインが完了したかどうか
//    ///
//    /// 以下2つの状態なら完了とする
//    ///  1. ログイン後のURLが指定したURLと一致していること
//    ///  2. ログイン処理中であるフラグが立っていること
//    ///  認められない場合は、falseとする
//    /// - Note:
//    /// - Parameter urlString: 現在表示しているURLString
//    /// - Returns: 判定結果、許可ならtrue
//    public fun isLoggedin(urlString: String): Boolean {
//        // ログイン後のURLが指定したURLと一致しているかどうか
//        val check1 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/TopEnqCheck.aspx")
//        val check2 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx")
//        val check3 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx")
//        // 上記から1つでもtrueがあれば、引き継ぐ
//        val result = check1 || check2 || check3
//        // ログイン処理中かつ、ログインURLと異なっている場合(URLが同じ場合はログイン失敗した状態)
//        if (isLoginProcessing && result) {
//            // ログイン処理を完了とする
//            isLoginProcessing = false
//            return true
//        }
//        return false
//    }

    /// タイムアウトのURLであるかどうかの判定
    /// - Parameter urlString: 読み込み完了したURLの文字列
    /// - Returns: 結果
//    fun isTimeout(urlString: String): Boolean {
//        if (urlString == "https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOut.aspx" ||
//            urlString == "https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOutSub.aspx") {
//            return true
//        }
//        return false
//    }
}