package com.example.tokumemo.domain.model

data class MenuDetailItem(
    val title: String,
    val id: Type,
    val targetUrl: String?
) {
    enum class Type {
        TimeTable,                      // 時間割
        CurrentTermPerformance,         // 今年の成績表
        Syllabus,                       // シラバス
        TermPerformance,                // 成績参照
        PresenceAbsenceRecord,          // 出欠記録
        ClassQuestionnaire,             // 授業アンケート
        SystemServiceList,              // システムサービス一覧
        ELearningList,                  // Eラーニング一覧
        CourseRegistration,             // 履修登録
        UniversityWeb,                  // 大学サイト
        Portal,                         // 統合認証ポータル

        LibraryWebHomePC,               // 図書館Webサイト常三島
        LibraryWebHomeKuraPC,           // 図書館Webサイト蔵本
        LibraryWebHomeMobile,
        LibraryMyPage,                  // 図書館MyPage
        LibraryBookLendingExtension,    // 図書館本貸出し期間延長
        LibraryBookPurchaseRequest,     // 図書館本購入リクエスト
        LibraryCalendarMain,            // 図書館カレンダー
        LibraryCalendarKura,

        CoopCalendar,                   // 徳島大学生活共同組合
        Cafeteria,                      // 徳島大学食堂
        CareerCenter,                   // キャリアセンター
        StudySupportSpace,              // 学びサポート企画部
        DisasterPrevention,             // 上月研究室防災情報
        SuperEnglish,                   // スーパー英語
    }
}