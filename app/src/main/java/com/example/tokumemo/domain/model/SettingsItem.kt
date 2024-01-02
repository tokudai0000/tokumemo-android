package com.example.tokumemo.domain.model

class SettingsItem(
    var title: String,
    var id : SettingListItemType,
    var url: String?
)

enum class SettingListItemType {
    Password,                       // パスワード

    AboutThisApp,                   // このアプリについて
    ContactUs,                      // お問い合わせ
    OfficialSNS,                    // 公式SNS
    HomePage,                       // ホームページ

    TermsOfService,                 // 利用規約
    PrivacyPolicy,                  // プライバシーポリシー
    SourceCode                      // ソースコード
}