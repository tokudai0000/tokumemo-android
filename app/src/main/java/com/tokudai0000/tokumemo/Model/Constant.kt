package com.tokudai0000.tokumemo

enum class MenuLists {
    courseManagementHomePC,
    courseManagementHomeMobile,
    manabaHomePC,
    manabaHomeMobile,
    libraryWebHomePC,               // 図書館Webサイト常三島
    libraryWebHomeKuraPC,           // 図書館Webサイト蔵本
    libraryWebHomeMobile,
    libraryMyPage,                  // 図書館MyPage
    libraryBookLendingExtension,    // 図書館本貸出し期間延長
    libraryBookPurchaseRequest,     // 図書館本購入リクエスト
    libraryCalendar,                // 図書館カレンダー
    syllabus,                       // シラバス
    timeTable,                      // 時間割
    currentTermPerformance,         // 今年の成績表
    termPerformance,                // 成績参照
    presenceAbsenceRecord,          // 出欠記録
    classQuestionnaire,             // 授業アンケート
    mailService,                    // メール
    tokudaiCareerCenter,            // キャリアセンター
    tokudaiCoop,                    // 徳島大学生活共同組合
    courseRegistration,             // 履修登録
    systemServiceList,              // システムサービス一覧
    eLearningList,                  // Eラーニング一覧
    universityWeb,                  // 大学サイト
    setting,                        // 設定へ

    customize,                      // 並び替え
    firstViewSetting,               // 初期画面設定
    password,                       // パスワード
    aboutThisApp,                   // このアプリについて
    buckToMenu,                     // 戻る
}

class Menu ( var title: String,               // 表示名ユーザーが変更することができる
             val id: MenuLists,
             val url: String,
             var isDisplay: Boolean,          // 初期値は全てfalse(ユーザー「常三島生、蔵本生」によって表示内容を変更させる)
             var isInitView: Boolean,         // 初期値は「マナバPC版」のみtrue
             val canInitView: Boolean )       // 初期画面として設定可能か

class Constant {
    companion object {
        /// 現在の利用規約バージョン
        val latestTermsVersion = "1.0.2"

        /// WebViewで読み込みを許可するドメイン
        val allowedDomains = arrayOf("tokushima-u.ac.jp",    // 大学サイト
                                    "office365.com",        // outlook(メール) ログイン画面
                                    "microsoftonline.com",  // outlook(メール) ログイン画面表示前、1度だけ遷移されるその後"office365.com"へ遷移される
                                    "office.com",           // outlook(メール) メールボックス
                                    "tokudai-syusyoku.com", // キャリア支援室
                                    "tokudai.marucoop.com", // 徳島大学生活共同組合
                                    "youtube.com")          // 大学サイトのインライン再生に対応させる為

        //ユーザーリストでデーターを追加
        val menuLists = arrayListOf<Menu> (
            Menu( "教務事務システム",
                MenuLists.courseManagementHomePC,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx",
                true,
                false,
                true),

            Menu( "マナバ",
                MenuLists.manabaHomePC,
                "https://manaba.lms.tokushima-u.ac.jp/ct/home",
                true,
                true,
                true),

            Menu( "図書貸し出し延長",
                MenuLists.libraryBookLendingExtension,
                "https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings",
                true,
                false,
                true),

            Menu( "開館カレンダー",
                MenuLists.libraryCalendar,
                "",
                false, // 今後実装予定
                false,
                true),

            Menu( "生協営業状況",
                MenuLists.tokudaiCoop,
                "https://tokudai.marucoop.com/#parts",
                true,
                false,
                true),

            Menu( "シラバス",
                MenuLists.syllabus,
                "",
                true,
                false,
                true),

            Menu( "今期の成績",
                MenuLists.currentTermPerformance,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year=",
                true,
                false,
                true),

            Menu( "メール",
                MenuLists.mailService,
                "https://outlook.office365.com/tokushima-u.ac.jp",
                true,
                false,
                true),

            // 以下デフォルトでは表示させない

            Menu("教務事務システム[PC]",
                MenuLists.courseManagementHomePC,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx",
                false,
                false,
                true),

            Menu("マナバ[Mobile]",
                MenuLists.manabaHomeMobile,
                "https://manaba.lms.tokushima-u.ac.jp/s/home_summary",
                false,
                false,
                true),

            Menu("図書Web[常三島]",
                MenuLists.libraryWebHomePC,
                "https://www.lib.tokushima-u.ac.jp/",
                false,
                false,
                true),

            Menu("図書Web[蔵本]",
                MenuLists.libraryWebHomePC,
                "https://www.lib.tokushima-u.ac.jp/kura.shtml",
                false,
                false,
                true),

            Menu("図書Web[Mobile]",
                MenuLists.libraryWebHomeMobile,
                "https://opac.lib.tokushima-u.ac.jp/drupal/",
                false,
                false,
                true),

            Menu("図書MyPage",
                MenuLists.libraryMyPage,
                "https://opac.lib.tokushima-u.ac.jp/opac/user/top",
                false,
                false,
                true),

            Menu("図書本購入リクエスト",
                MenuLists.libraryBookPurchaseRequest,
                "https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings",
                false,
                false,
                true),

            Menu("時間割",
                MenuLists.timeTable,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx",
                false,
                false,
                true),

            Menu("成績参照",
                MenuLists.termPerformance,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Menu.aspx",
                true,
                false,
                true),

            Menu("出欠記録",
                MenuLists.presenceAbsenceRecord,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Attendance/AttendList.aspx",
                false,
                false,
                true),

            Menu("授業アンケート",
                MenuLists.classQuestionnaire,
                "https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Enquete/EnqAnswerList.aspx",
                false,
                false,
                true),

            Menu("キャリア支援室",
                MenuLists.tokudaiCareerCenter,
                "https://www.tokudai-syusyoku.com/index.php",
                true,
                false,
                true),

            Menu("システムサービス一覧",
                MenuLists.systemServiceList,
                "https://www.ait.tokushima-u.ac.jp/service/list_out/",
                true,
                false,
                true),

            Menu("Eラーニング一覧",
                MenuLists.eLearningList,
                "https://uls01.ulc.tokushima-u.ac.jp/info/index.html",
                true,
                false,
                true),

            Menu("大学サイト",
                MenuLists.universityWeb,
                "https://www.tokushima-u.ac.jp/",
                true,
                false,
                true),

            // 設定類

            Menu( "設定 ＞",
                MenuLists.setting,
                "",
                true,
                false,
                true),
        )

        //ユーザーリストでデーターを追加
        val initSettingLists = arrayListOf<Menu> (
            Menu( "パスワード",
                MenuLists.password,
                "",
                true,
                false,
                false),

            // 今後実装予定
//            Menu( "カスタマイズ",
//                MenuLists.customize,
//                "",
//                true,
//                false,
//                false),
//
//            Menu( "初期画面設定",
//                MenuLists.firstViewSetting,
//                "",
//                true,
//                false,
//                false),

            Menu( "このアプリについて",
                MenuLists.aboutThisApp,
                "https://github.com/tokudai0000/document/tree/main/tokumemo/terms",
                true,
                false,
                false),

            Menu( "＜ 戻る",
                MenuLists.buckToMenu,
                "",
                true,
                false,
                false),
        )
    }

}