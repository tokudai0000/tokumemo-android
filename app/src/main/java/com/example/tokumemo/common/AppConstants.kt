package com.example.tokumemo.common

import com.example.tokumemo.domain.model.SettingListItemType
import com.example.tokumemo.domain.model.SettingsListData

object AppConstants {

    val settingsItems = listOf(
        SettingsListData(title="パスワード", id= SettingListItemType.Password, url= null),
        SettingsListData(title="このアプリについて", id= SettingListItemType.AboutThisApp, url= Url.AppIntroduction.urlString),
        SettingsListData(title="お問い合わせ", id= SettingListItemType.ContactUs, url= Url.ContactUs.urlString),
        SettingsListData(title="公式SNS", id= SettingListItemType.OfficialSNS, url= Url.OfficialSNS.urlString),
        SettingsListData(title="ホームページ", id= SettingListItemType.HomePage, url= Url.HomePage.urlString),
        SettingsListData(title="利用規約", id= SettingListItemType.TermsOfService, url= Url.TermsOfService.urlString),
        SettingsListData(title="プライバシーポリシー", id= SettingListItemType.PrivacyPolicy, url= Url.PrivacyPolicy.urlString),
        SettingsListData(title="ソースコード", id= SettingListItemType.SourceCode, url= Url.SourceCode.urlString)
    )
}