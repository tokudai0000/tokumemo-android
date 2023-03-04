package com.example.tokumemo.main.home

import android.view.Menu

enum class MenuListItemType {
    courseManagementHomePC,         // 教務事務システム
    courseManagementHomeMobile,
    manabaHomePC,                   // マナバ
    manabaHomeMobile,
    portal,                         // 統合認証ポータル
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
    careerCenter,                   // キャリアセンター
    coopCalendar,                   // 徳島大学生活共同組合
    cafeteria,                      // 徳島大学食堂
    courseRegistration,             // 履修登録
    systemServiceList,              // システムサービス一覧
    eLearningList,                  // Eラーニング一覧
    universityWeb,                  // 大学サイト
    studySupportSpace,              // 学びサポート企画部
    disasterPrevention,             // 上月研究室防災情報

    favorite,                       // お気に入り
}

data class MenuData(
    var title: String,
    var id: MenuListItemType,
    var image: android.graphics.drawable.Drawable,
    var url: String?,
    var isLockIconExists: Boolean,
    var isHiddon: Boolean
)