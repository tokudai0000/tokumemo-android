package com.example.tokumemo.main.settings

enum class SettingListItemType {
    password,                       // パスワード
    favorite,                       // お気に入り登録
    customize,                      // 並び替え

    aboutThisApp,                   // このアプリについて
    contactUs,                      // お問い合わせ
    officialSNS,                    // 公式SNS
    homePage,                       // ホームページ

    termsOfService,                 // 利用規約
    privacyPolicy,                  // プライバシーポリシー
    sourceCode                      // ソースコード
}

data class SettingsData(
    var title: String,
    var id : SettingListItemType,
    var url: String
)