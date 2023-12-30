package com.example.tokumemo.ui.home

data class HomeListData(
    var title: String,
    var id: MenuListItemType,
    var image: Int,
    var url: String?,
    var isLockIconExists: Boolean,
    var isHidden: Boolean
)

enum class MenuListItemType {
    CourseManagementHomePC,         // 教務事務システム
    CourseManagementHomeMobile,
    ManabaHomePC,                   // マナバ
    ManabaHomeMobile,
    Portal,                         // 統合認証ポータル
    LibraryWebHomePC,               // 図書館Webサイト常三島
    LibraryWebHomeKuraPC,           // 図書館Webサイト蔵本
    LibraryWebHomeMobile,
    LibraryMyPage,                  // 図書館MyPage
    LibraryBookLendingExtension,    // 図書館本貸出し期間延長
    LibraryBookPurchaseRequest,     // 図書館本購入リクエスト
    LibraryCalendar,                // 図書館カレンダー
    Syllabus,                       // シラバス
    TimeTable,                      // 時間割
    CurrentTermPerformance,         // 今年の成績表
    TermPerformance,                // 成績参照
    PresenceAbsenceRecord,          // 出欠記録
    ClassQuestionnaire,             // 授業アンケート
    MailService,                    // メール
    CareerCenter,                   // キャリアセンター
    CoopCalendar,                   // 徳島大学生活共同組合
    Cafeteria,                      // 徳島大学食堂
    CourseRegistration,             // 履修登録
    SystemServiceList,              // システムサービス一覧
    ELearningList,                  // Eラーニング一覧
    UniversityWeb,                  // 大学サイト
    StudySupportSpace,              // 学びサポート企画部
    DisasterPrevention,             // 上月研究室防災情報

    Favorite,                       // お気に入り
}