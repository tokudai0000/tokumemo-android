package com.example.tokumemo

enum class Url(val urlString: String) {
    /// 大学ホームページ
    UniversityHomePage("https://www.tokushima-u.ac.jp/"),
    /// 統合認証システム
    Portal("https://my.ait.tokushima-u.ac.jp/portal/"),
    /// Eラーニング一覧
    ELearningList("https://uls01.ulc.tokushima-u.ac.jp/info/index.html"),

    /// 教務事務システム(PC)
    CourseManagementPC("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Top.aspx"),
    /// 教務事務システム(Mobile)
    CourseManagementMobile("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/sp/Top.aspx"),
    /// マナバ(PC)
    ManabaPC("https://manaba.lms.tokushima-u.ac.jp/ct/home"),
    /// マナバ(Mobile)
    ManabaMobile("https://manaba.lms.tokushima-u.ac.jp/s/home_summary"),

    /// 講義時間割
    TimeTable("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistList.aspx"),
    /// 今期の成績表
    CurrentTermPerformance("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Sp/ReferResults/SubDetail/Results_Get_YearTerm.aspx?year="),
    /// 成績選択画面
    TermPerformance("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/ReferResults/Menu.aspx"),
    /// 出欠記録
    PresenceAbsenceRecord("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Attendance/AttendList.aspx"),
    /// 授業評価アンケート
    ClassQuestionnaire("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Enquete/EnqAnswerList.aspx"),
    /// 履修登録
    CourseRegistration("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/Regist/RegistEdit.aspx"),

    /// 図書館サイト(本館PC)
    LibraryHomePageMainPC("https://www.lib.tokushima-u.ac.jp/"),
    /// 図書館サイト(蔵本PC)
    LibraryHomePageKuraPC("https://www.lib.tokushima-u.ac.jp/kura.shtml"),
    /// 図書館サイト(Mobile)
    LibraryHomeMobile("https://opac.lib.tokushima-u.ac.jp/drupal/"),
    /// MyPage
    LibraryMyPage("https://opac.lib.tokushima-u.ac.jp/opac/user/top"),
    /// 本貸出し期間延長
    LibraryBookLendingExtension("https://opac.lib.tokushima-u.ac.jp/opac/user/holding-borrowings"),
    /// 本購入リクエスト
    LibraryBookPurchaseRequest("https://opac.lib.tokushima-u.ac.jp/opac/user/purchase_requests/new"),

    /// outlook(メール)複数回遷移したのち、outlookLoginFormへ行く
    OutlookService("https://outlook.office365.com/tokushima-u.ac.jp"),
    /// ログイン画面
    OutlookLoginForm("https://wa.tokushima-u.ac.jp/adfs/ls"),

    /// シラバス
    Syllabus("http://eweb.stud.tokushima-u.ac.jp/Portal/Public/Syllabus/"),

    /// 徳島大学キャリアセンター
    TokudaiCareerCenter("https://www.tokudai-syusyoku.com/index.php"),

    /// 徳島大学生活共同組合　旧"https://tokudai.marucoop.com/#parts"
    TokudaiCoop("https://vsign.jp/tokudai/maruco#parts"),

    /// 徳島大学生協食堂メニュー
    TokudaiCoopDinigMenu("https://tokudaicoop.jp/food2.html"),

    /// 学びサポート企画部
    StudySupportSpace("https://www.lib.tokushima-u.ac.jp/support/sss/index.html"),

    /// 防災情報
    DisasterPrevention("https://www.tokushima-u.ac.jp/rcmode/business/46584.html"),

    /// ログイン画面に遷移する為のURL(何度もURL遷移を行う)
    UniversityTransitionLogin("http://eweb.stud.tokushima-u.ac.jp/Portal/top.html"),
    /// 大学サイト、ログイン画面
    UniversityLogin("https://localidp.ait230.tokushima-u.ac.jp/idp/profile/SAML2/Redirect/SSO?execution="),
    /// タイムアウト(20分無操作)
    UniversityServiceTimeOut("https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOut.aspx"),
    /// タイムアウト2(20分無操作)
    UniversityServiceTimeOut2("https://eweb.stud.tokushima-u.ac.jp/Portal/RichTimeOutSub.aspx"),
    /// アンケート催促画面(教務事務表示前に出現)
    SkipReminder("https://eweb.stud.tokushima-u.ac.jp/Portal/StudentApp/TopEnqCheck.aspx"),
    /// マナバから授業動画[Youtube]を開く時
    PopupToYoutube("https://manaba.lms.tokushima-u.ac.jp/s/link_balloon"),
    /// GoogleFormsでお問い合わせフォームを作成
    ContactUs("https://docs.google.com/forms/d/e/1FAIpQLScYRhlWY9IjqWOrvnWJ0bJ_yPQZpXy4PPShWb68092t2klzeg/viewform"),
    /// TwitterのURL
    OfficialSNS("https://twitter.com/tokumemo0000"),
    /// ホームページのURL
    HomePage("https://lit.link/developers"),
    /// トクメモ＋のプライバシーポリシーURL[GitHub]
    PrivacyPolicy("https://tokudai0000.github.io/tokumemo_resource/document/privacy_policy.txt"),
    /// トクメモ＋の利用規約URL[GitHub]
    TermsOfService("https://tokudai0000.github.io/tokumemo_resource/document/terms_of_service.txt"),
    /// アプリ紹介文
    AppIntroduction("https://tokudai0000.github.io/tokumemo_resource/document/tokumemo_explanation.txt"),
    /// トクメモ＋のソースコード[GitHub]
    SourceCode("https://github.com/tokudai0000/TokumemoAndroid"),
    /// 気象庁の天気予報
    Weather("https://www.jma.go.jp/bosai/#area_type=class20s&area_code=3620100&pattern=forecast"),

    /// 徳大学生活動リスト
    ClubList("https://tokudai0000.github.io/club-list/"),

    /// 徳大ニュース用のRSSをJsonに変換してくれるサイト
    Rss("https://api.rss2json.com/v1/api.json?rss_url=https://www.tokushima-u.ac.jp/recent/rss.xml")

}