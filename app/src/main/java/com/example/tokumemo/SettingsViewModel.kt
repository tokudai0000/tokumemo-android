package com.example.tokumemo

import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {

    var initSettingsList = listOf(
        SettingsListData(title="教務事務システム", id= SettingListItemType.Password, url= null),
        SettingsListData(title="お気に入り登録", id= SettingListItemType.Favorite, url= null),
        SettingsListData(title="カスタマイズ", id= SettingListItemType.Customize, url= null),
        SettingsListData(title="このアプリについて", id= SettingListItemType.AboutThisApp, url= null),
        SettingsListData(title="お問い合わせ", id= SettingListItemType.ContactUs, url= Url.ContactUs.urlString),
        SettingsListData(title="公式SNS", id= SettingListItemType.OfficialSNS, url= Url.OfficialSNS.urlString),
        SettingsListData(title="ホームページ", id= SettingListItemType.HomePage, url= Url.HomePage.urlString),
        SettingsListData(title="利用規約", id= SettingListItemType.TermsOfService, url= Url.TermsOfService.urlString),
        SettingsListData(title="プライバシーポリシー", id= SettingListItemType.PrivacyPolicy, url= Url.PrivacyPolicy.urlString),
        SettingsListData(title="ソースコード", id= SettingListItemType.SourceCode, url= Url.SourceCode.urlString)
    )

}