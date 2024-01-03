package com.example.tokumemo.common

enum class Url(val urlString: String) {

    /// ログイン画面に遷移する為のURL(何度もURL遷移を行う) 旧http://eweb.stud.tokushima-u.ac.jp/Portal/top.html
    UniversityTransitionLogin("http://eweb.stud.tokushima-u.ac.jp/Portal/top.html"),


    // ----- JavaScript実行検知 関連 -----
    /// 大学サイト、ログイン画面
    UniversityLogin("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution="),
    /// タイムアウト(20分無操作)
    UniversityServiceTimeOut("https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOut.aspx"),
    /// タイムアウト2(20分無操作)
    UniversityServiceTimeOut2("https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOutSub.aspx"),
    /// 検索完了後のシラバス
    SyllabusSearchCompleted("http://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/SearchMain.aspx"),
    /// アンケート催促画面(教務事務表示前に出現)
    SkipReminder("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/TopEnqCheck.aspx"),


    // ----- 大学教務 関連 -----
    /// 教務事務システム(PC)
    CourseManagementPC("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx"),
    /// 教務事務システム(Mobile)
    CourseManagementMobile("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx"),
    /// マナバ(PC)
    ManabaPC("https://manaba.lms.tokushima-u.ac.jp/ct/home"),
    /// マナバ(Mobile)
    ManabaMobile("https://manaba.lms.tokushima-u.ac.jp/s/home_summary"),
    /// outlook(メール)複数回遷移したのち、outlookLoginFormへ行く
    OutlookService("https://outlook.office365.com/tokushima-u.ac.jp"),
    /// ログイン画面
    OutlookLoginForm("https://wa.tokushima-u.ac.jp/adfs/ls"),


    // ----- 講義 関連 -----
    /// 講義時間割 旧:https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx
    TimeTable("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/Schedule/Day.aspx"),
    /// 今期の成績表
    CurrentTermPerformance("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx"),
    /// シラバス
    Syllabus("http://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/"),
    /// 出欠記録
    PresenceAbsenceRecord("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Attendance/AttendList.aspx"),
    /// 全ての成績選択画面
    TermPerformance("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Menu.aspx"),


    // ----- 図書館 関連 -----
    /// 図書館サイト(本館PC)
    LibraryHomePageMainPC("https://www.lib.tokushima-u.ac.jp/"),
    /// 図書館サイト(蔵本PC)
    LibraryHomePageKuraPC("https://www.lib.tokushima-u.ac.jp/kura.shtml"),
    /// 本貸出し期間延長
    LibraryBookLendingExtension("https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings"),
    /// 本購入リクエスト
    LibraryBookPurchaseRequest("https://opac.lib.tokushima-u.ac.jp/opac/user/purchase_requests/new"),
    /// MyPage
    LibraryMyPage("https://opac.lib.tokushima-u.ac.jp/opac/user/top"),


    // ----- その他 関連 -----
    /// 徳島大学生活共同組合　旧"https://tokudai.marucoop.com/#parts"
    TokudaiCoop("https://vsign.jp/tokudai/maruco#parts"),
    /// 徳島大学生協食堂メニュー
    TokudaiCoopDinigMenu("https://tokudaicoop.jp/food2.html"),
    /// 徳島大学キャリアセンター
    TokudaiCareerCenter("https://www.tokudai-syusyoku.com/index.php"),
    /// 学びサポート企画部
    StudySupportSpace("https://www.lib.tokushima-u.ac.jp/support/sss/index.html"),
    /// 防災情報
    DisasterPrevention("https://www.tokushima-u.ac.jp/rcmode/business/46584.html"),


    // ----- ホーム、ニュース、部活動 -----
    PrItemJsonData("https://tokudai0000.github.io/tokumemo_resource/pr_image/info.json"),
    NewsItemJsonData("https://api.rss2json.com/v1/api.json?rss_url=https://www.tokushima-u.ac.jp/recent/rss.xml"),
    ClubLists("https://tokudai0000.github.io/club-list/"),


    // ----- 設定 関連 -----
    /// GoogleFormsでPR画像申請フォームを作成
    PrApplication("https://docs.google.com/forms/d/e/1FAIpQLSeyQ-rpMslvLp4WSLc1JsyI9skve_WYYNp61G1zlRAdGKVlBQ/viewform"),
    /// GoogleFormsでお問い合わせフォームを作成
    ContactUs("https://docs.google.com/forms/d/e/1FAIpQLScYRhlWY9IjqWOrvnWJ0bJ_yPQZpXy4PPShWb68092t2klzeg/viewform"),
    /// X(Twitter)
    OfficialSNS("https://twitter.com/tokumemo0000"),
    /// ホームページ
    HomePage("https://tokumemo.notion.site/6e750dcdc6d544a9a23460503d88ca5d"),
    /// トクメモ＋のプライバシーポリシー
    PrivacyPolicy("https://tokumemo-store-house.notion.site/21abf42ea3134a07b5df41f206df1e5d"),
    /// トクメモ＋の利用規約
    TermsOfService("https://tokumemo-store-house.notion.site/5c8ed54b641d481993f1ee9be8709aea"),
    /// アプリ紹介文
    AppIntroduction("https://tokumemo.notion.site/945a1f46d3794d37b1317d668a98728b"),
    /// トクメモ＋のソースコード[GitHub]
    SourceCode("https://github.com/tokudai0000/TokumemoAndroid"),
    /// トクメモ＋のGitHub
    GitHub("https://github.com/tokudai0000"),
    /// トクメモ＋アプリのAppStoreレビュー画面
//    Review("https://apps.apple.com/app/id1582738889?action=write-review"),


    /// 何もリクエストしない(エラー用) UnitTest時には除外してTestする
    EmptyRequest("about:blank"),


    // ----- 未使用 -----
    /// 図書館サイト(Mobile)
    LibraryHomeMobile("https://opac.lib.tokushima-u.ac.jp/drupal/"),
    /// 授業評価アンケート
    ClassQuestionnaire("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Enquete/EnqAnswerList.aspx"),
    /// 履修登録
    CourseRegistration("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistEdit.aspx"),
    /// スーパー英語
    SuperEnglishLogin("https://tse.ait231.tokushima-u.ac.jp/student/main/login/"),
    /// 大学ホームページ
    UniversityHomePage("https://www.tokushima-u.ac.jp/"),
    /// 統合認証システム
    Portal("https://my.ait.tokushima-u.ac.jp/portal/"),
    /// Eラーニング一覧
    ELearningList("https://uls01.ulc.tokushima-u.ac.jp/info/index.html"),
    /// マナバから授業動画[Youtube]を開く時
    PopupToYoutube("https://manaba.lms.tokushima-u.ac.jp/s/link_balloon"),
    /// 気象庁の天気予報
    Weather("https://www.jma.go.jp/bosai/#area_type=class20s&area_code=3620100&pattern=forecast"),
}