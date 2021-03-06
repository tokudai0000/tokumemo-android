package com.tokudai0000.tokumemo.ui.main

import androidx.lifecycle.ViewModel
import com.tokudai0000.tokumemo.Constant
import com.tokudai0000.tokumemo.Model.DataManager

class MainModel: ViewModel() {

    /// Favorite画面へURLを渡すのに使用
    public var urlString = ""

    /// シラバスをJavaScriptで自動入力する際、参照変数
    public var subjectName = ""
    public var teacherName = ""

    /// ログイン処理中かどうか
    public var isLoginProcessing = false

    /// 最新の利用規約同意者か判定し、同意画面の表示を行うべきか判定
//    public val shouldShowTermsAgreementView: Boolean = dataManager.agreementVersion != Constant.latestTermsVersion

    /// URLの読み込みを許可するか判定
    /// ドメイン名が許可しているのと一致しているなら許可(ホワイトリスト制)
    /// - Parameter url: 判定したいURL
    /// - Returns: 判定結果、許可ならtrue
    public fun isAllowedDomainCheck(urlString: String): Boolean {

        // ドメインを検証
        for (item in Constant.allowedDomains) {
            val regex = Regex(item)
            // 部分一致
            if (urlString.contains(regex)) {
                // 一致したなら
                return true
            }
        }
        return false
    }

    /// JavaScriptを動かす種類
    enum class JavaScriptType {
        syllabus, // シラバスの検索画面
        loginIAS, // 大学統合認証システム(IAS)のログイン画面
        loginOutlook, // メール(Outlook)のログイン画面
        loginCareerCenter, // 徳島大学キャリアセンターのログイン画面
        none, // JavaScriptを動かす必要がない場合
    }
    /// JavaScriptを動かしたい指定のURLかどうかを判定し、動かすJavaScriptの種類を返す
    ///
    /// - Note: canExecuteJavascriptが重要な理由
    ///   ログインに失敗した場合に再度ログインのURLが表示されることになる。
    ///   canExecuteJavascriptが存在しないと、再度ログインの為にJavaScriptが実行され続け無限ループとなってしまう。
    /// - Parameter urlString: 読み込み完了したURLの文字列
    /// - Returns: 動かすJavaScriptの種類
    public fun anyJavaScriptExecute(urlString: String): JavaScriptType {
        // JavaScriptを実行するフラグが立っていない場合はnoneを返す
        if (DataManager.canExecuteJavascript == false) {
            return JavaScriptType.none
        }
        // シラバスの検索画面
        if (urlString == "http://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/") {
            return JavaScriptType.syllabus
        }
        // cアカウント、パスワードを登録しているか
//        if (hasRegisteredPassword == false) {
//            return JavaScriptType.none
//        }
        // 大学統合認証システム(IAS)のログイン画面
        if (urlString.startsWith("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=", 0)) {
            return JavaScriptType.loginIAS
        }
        // メール(Outlook)のログイン画面
        if (urlString.startsWith("https://wa.tokushima-u.ac.jp/adfs/ls", 0)) {
            return JavaScriptType.loginOutlook
        }
        // 徳島大学キャリアセンターのログイン画面
        if (urlString == "https://www.tokudai-syusyoku.com/index.php") {
            return JavaScriptType.loginCareerCenter
        }
        // それ以外なら
        return JavaScriptType.none
    }

    /// 初期画面に指定したWebページのURLを返す
    ///
    /// ログイン処理完了後に呼び出される。つまり、ログインが完了したユーザーのみが呼び出す。
    /// structure Menu に存在するisInitViewがtrueであるのを探し、そのURLRequestを返す
    /// 何も設定していないユーザーはマナバ(初期値)を表示させる。
    /// - Note:
    ///   isInitViewは以下の1つの事例を除き、必ずtrueは存在する。
    ///   1. お気に入り登録内容を初期設定画面に登録し、カスタマイズから削除した場合
    /// - Returns: 設定した初期画面のURLの文字列
    public fun searchInitPageUrl(): String {
        // 初期設定画面のURLを読み込む
        for (menuList in Constant.menuLists) {
            // ユーザーが指定した初期画面を探す
            if (menuList.isInitView) {
                // メニューリストの内1つだけ、isInitView=trueが存在する
                return menuList.url.toString()
            }
        }
        // 見つからなかった場合
        // お気に入り画面を初期画面に設定しており、カスタマイズから削除した可能性がある為
        // マナバを表示させる
        return "https://manaba.lms.tokushima-u.ac.jp/ct/home"
    }

    /// 大学統合認証システム(IAS)へのログインが完了したかどうか
    ///
    /// 以下2つの状態なら完了とする
    ///  1. ログイン後のURLが指定したURLと一致していること
    ///  2. ログイン処理中であるフラグが立っていること
    ///  認められない場合は、falseとする
    /// - Note:
    /// - Parameter urlString: 現在表示しているURLString
    /// - Returns: 判定結果、許可ならtrue
    public fun isLoggedin(urlString: String): Boolean {
        // ログイン後のURLが指定したURLと一致しているかどうか
        val check1 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/TopEnqCheck.aspx")
        val check2 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx")
        val check3 = urlString.contains("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx")
        // 上記から1つでもtrueがあれば、引き継ぐ
        val result = check1 || check2 || check3
        // ログイン処理中かつ、ログインURLと異なっている場合(URLが同じ場合はログイン失敗した状態)
        if (isLoginProcessing && result) {
            // ログイン処理を完了とする
            isLoginProcessing = false
            return true
        }
        return false
    }

    /// 現在の時刻を保存する
//    public func saveCurrentTime() {
//        // 現在の時刻を取得し保存
//        let f = DateFormatter()
//        f.setTemplate(.full)
//        let now = Date()
//        dataManager.setUserDefaultsString(key: KEY_saveCurrentTime, value: f.string(from: now))
//    }

    /// 再度ログイン処理を行うかどうか
    ///
    /// - Returns: 判定結果、行うべきならtrue
//    public fun isExecuteLogin(): Boolean {
//
//        let formatter: DateFormatter = DateFormatter()
//        formatter.dateFormat = "MM/dd/yyyy, HH:mm:ss"
//        let lastTime = formatter.date(from: dataManager.getUserDefaultsString(key: KEY_saveCurrentTime))
//        guard let lastTime = lastTime else {
//            return false
//        }
//
//        // 現在の時刻を取得
//        let now = Date()
//        print(now.timeIntervalSince(lastTime))
//        // 時刻の差分が30*60秒以上であれば再ログインを行う
//        if now.timeIntervalSince(lastTime) > 30 * 60 {
//            return true
//        }
//        return false
//    }

    /// タイムアウトのURLであるかどうかの判定
    /// - Parameter urlString: 読み込み完了したURLの文字列
    /// - Returns: 結果
    public fun isTimeout(urlString: String): Boolean {
        if (urlString == "https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOut.aspx" ||
                urlString == "https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOutSub.aspx") {
            return true
        }
        return false
    }

    // MARK: - Private

//    private val dataManager = DataManager.singleton

    // cアカウント、パスワードを登録しているか判定
//    private val hasRegisteredPassword: Boolean = (!(dataManager.cAccount.isEmpty || dataManager.password.isEmpty))

    // 前回利用した時間を保存
//    private val KEY_saveCurrentTime = "KEY_saveCurrentTime"
}