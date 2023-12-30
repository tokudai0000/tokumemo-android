package com.example.tokumemo.ui.settings

data class SettingsListData(
    var title: String,
    var id : SettingListItemType,
    var url: String?
)

enum class SettingListItemType {
    Password,                       // パスワード
    Favorite,                       // お気に入り登録
    Customize,                      // 並び替え

    AboutThisApp,                   // このアプリについて
    ContactUs,                      // お問い合わせ
    OfficialSNS,                    // 公式SNS
    HomePage,                       // ホームページ

    TermsOfService,                 // 利用規約
    PrivacyPolicy,                  // プライバシーポリシー
    SourceCode                      // ソースコード
}