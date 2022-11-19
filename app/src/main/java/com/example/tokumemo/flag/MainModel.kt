package com.example.tokumemo.flag

import androidx.lifecycle.ViewModel
import com.example.tokumemo.manager.DataManager

class MainModel: ViewModel() {

    /// ログイン処理中かどうか
    public var isLoginProcessing = false

    /// JavaScriptを動かす種類
    enum class JavaScriptType {
        loginIAS, // 大学統合認証システム(IAS)のログイン画面
        none, // JavaScriptを動かす必要がない場合
    }
    /// JavaScriptを動かしたい指定のURLかどうかを判定し、動かすJavaScriptの種類を返す
    ///
    /// - Note: canExecuteJavascriptが重要な理由
    ///   ログインに失敗した場合に再度ログインのURLが表示されることになる。
    ///   canExecuteJavascriptが存在しないと、再度ログインの為にJavaScriptが実行され続け無限ループとなってしまう。
    /// - Parameter urlString: 読み込み完了したURLの文字列
    /// - Returns: 動かすJavaScriptの種類

//    public fun isAnyWebsite(pageId: Int): String {
//
//        var url = ""
//
//        when (pageId) {
//            // キャリアセンター
//            0 -> {
//                url = "https://www.tokudai-syusyoku.com/index.php"
//            }
//            // マナバ
//            1 -> {
//                url = "https://manaba.lms.tokushima-u.ac.jp/ct/home"
//            }
//            // 教務システム
//            2 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx"
//            }
//            // メール
//            3 -> {
//                url = "https://outlook.office365.com/mail/"
//            }
//            // 図書館
//            4 -> {
//                url = "https://opac.lib.tokushima-u.ac.jp/opac/user/top"
//            }
//            // 生協
//            5 -> {
//                url = "https://vsign.jp/tokudai/maruco"
//            }
//            // 成績
//            6 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Results.aspx"
//            }
//            // 時間割
//            7 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx"
//            }
//            // シラバス
//            8 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/SearchMain.aspx"
//            }
//            // 図書館カレンダー(常三島)
//            9 -> {
//                url = "https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_2022.pdf"
//            }
//            // 図書館カレンダー(蔵本)
//            10 -> {
//                url = "https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_2022.pdf"
//            }
//            // 本貸出延長
//            11 -> {
//                url = "https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings"
//            }
//            // 全学期の成績
//            12 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Menu.aspx"
//            }
//            // 図書館サイト
//            13 -> {
//                url = "https://opac.lib.tokushima-u.ac.jp/drupal/"
//            }
//            // 本購入依頼
//            14 -> {
//                url = "https://opac.lib.tokushima-u.ac.jp/opac/user/purchase_requests/new"
//            }
//            // LMS一覧
//            15 -> {
//                url = "https://uls01.ulc.tokushima-u.ac.jp/info/index.html"
//            }
//            // 常三島図書館HP
//            16 -> {
//                url = "https://www.lib.tokushima-u.ac.jp/"
//            }
//            // 蔵本図書館HP
//            17 -> {
//                url = "https://www.lib.tokushima-u.ac.jp/kura.shtml"
//            }
//            // 教務システム_PC
//            18 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx"
//            }
//            // マナバ_モバイル
//            19 -> {
//                url = "https://manaba.lms.tokushima-u.ac.jp/s/home_summary"
//            }
//            // 総合認証ポータル
//            20 -> {
//                url = "https://my.ait.tokushima-u.ac.jp/portal/"
//            }
//            // 大学サイト
//            21 -> {
//                url = "https://www.tokushima-u.ac.jp/"
//            }
//            // 出欠
//            22 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Attendance/AttendList.aspx"
//            }
//            // 授業アンケート
//            23 -> {
//                url = "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Enquete/EnqAnswerList.aspx"
//            }
//            // 天気予報
//            24 -> {
//                url = "https://www.nhk.or.jp/kishou-saigai/city/weather/36201003620100/#anaten-area-name"
//            }
//            else -> {}
//        }
//
//        return url
//    }

    public fun anyJavaScriptExecute(urlString: String): JavaScriptType {
        // 大学統合認証システム(IAS)のログイン画面
        if (urlString.startsWith("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution=", 0)) {
            DataManager.canExecuteJavascript = true
            return JavaScriptType.loginIAS
        }
        // JavaScriptを実行するフラグが立っていない場合はnoneを返す
        if (DataManager.canExecuteJavascript == false) {
            return JavaScriptType.none
        }
        // それ以外なら
        return JavaScriptType.none
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
}