package com.example.tokumemo.common

import com.example.tokumemo.R
import com.example.tokumemo.domain.model.MenuDetailItem
import com.example.tokumemo.domain.model.MenuItem
import com.example.tokumemo.domain.model.SettingListItemType
import com.example.tokumemo.domain.model.SettingsItem

object AppConstants {
    val menuItems: List<MenuItem>
    val academicRelatedItems: List<MenuDetailItem>
    val libraryRelatedItems: List<MenuDetailItem>
    val etcItems: List<MenuDetailItem>
    val homeMiniSettingsItems: List<SettingsItem>
    val settingsItems: List<SettingsItem>

    init {
        menuItems = listOf(
            MenuItem(title="教務事務システム", id= MenuItem.Type.CourseManagement, image= R.drawable.coursemanagementhome, url= Url.CourseManagementMobile.urlString),
            MenuItem(title="manaba", id= MenuItem.Type.Manaba, image= R.drawable.manaba, url= Url.ManabaPC.urlString),
            MenuItem(title="メール", id= MenuItem.Type.Mail, image= R.drawable.mailservice, url= Url.OutlookService.urlString),
            MenuItem(title="講義関連", id= MenuItem.Type.AcademicRelated, image= R.drawable.librarybooklendingextension, url=null),
            MenuItem(title="図書関連", id= MenuItem.Type.LibraryRelated, image= R.drawable.librarybooklendingextension, url=null),
            MenuItem(title="その他", id= MenuItem.Type.Etc, image= R.drawable.librarybooklendingextension, url=null),
        )

        academicRelatedItems = listOf(
            MenuDetailItem(title="時間割", id= MenuDetailItem.Type.TimeTable, targetUrl= Url.TimeTable.urlString),
            MenuDetailItem(title="今学期の成績", id= MenuDetailItem.Type.CurrentTermPerformance, targetUrl= Url.CurrentTermPerformance.urlString),
            MenuDetailItem(title="シラバス", id= MenuDetailItem.Type.Syllabus, targetUrl= Url.CurrentTermPerformance.urlString),
            MenuDetailItem(title="出欠記録", id= MenuDetailItem.Type.PresenceAbsenceRecord, targetUrl= Url.PresenceAbsenceRecord.urlString),
            MenuDetailItem(title="すべての成績", id= MenuDetailItem.Type.TermPerformance, targetUrl= Url.TermPerformance.urlString),
        )

        libraryRelatedItems = listOf(
            MenuDetailItem(title="常三島図書館 カレンダー", id= MenuDetailItem.Type.LibraryCalendarMain, targetUrl=null),
            MenuDetailItem(title="蔵本図書館 カレンダー", id= MenuDetailItem.Type.LibraryCalendarKura, targetUrl=null),
            MenuDetailItem(title="貸出図書の期間延長", id= MenuDetailItem.Type.LibraryBookLendingExtension, targetUrl= Url.LibraryBookLendingExtension.urlString),
            MenuDetailItem(title="本の購入リクエスト", id= MenuDetailItem.Type.LibraryBookPurchaseRequest, targetUrl= Url.LibraryBookPurchaseRequest.urlString),
            MenuDetailItem(title="マイページ", id= MenuDetailItem.Type.LibraryMyPage, targetUrl= Url.LibraryBookPurchaseRequest.urlString),
        )

        etcItems = listOf(
            MenuDetailItem(title="生協営業時間割", id= MenuDetailItem.Type.CoopCalendar, targetUrl= Url.TokudaiCoop.urlString),
            MenuDetailItem(title="食堂メニュー", id= MenuDetailItem.Type.Cafeteria, targetUrl= Url.TokudaiCoopDinigMenu.urlString),
            MenuDetailItem(title="キャリア支援室", id= MenuDetailItem.Type.CareerCenter, targetUrl= Url.TokudaiCareerCenter.urlString),
            MenuDetailItem(title="SSS学習支援室の時間割", id= MenuDetailItem.Type.StudySupportSpace, targetUrl= Url.StudySupportSpace.urlString),
            MenuDetailItem(title="命を守る防災知識", id= MenuDetailItem.Type.DisasterPrevention, targetUrl= Url.DisasterPrevention.urlString),
        )

        homeMiniSettingsItems = listOf(
            SettingsItem(title="PR画像(広告)の利用申請", id= SettingListItemType.ContactUs, url= Url.ContactUs.urlString),
            SettingsItem(title="お問い合わせ", id= SettingListItemType.ContactUs, url= Url.ContactUs.urlString),
            SettingsItem(title="ホームページ", id= SettingListItemType.HomePage, url= Url.HomePage.urlString),
            SettingsItem(title="利用規約", id= SettingListItemType.TermsOfService, url= Url.TermsOfService.urlString),
            SettingsItem(title="プライバシーポリシー", id= SettingListItemType.PrivacyPolicy, url= Url.PrivacyPolicy.urlString),
//            SettingsItem(title="レビュって", id= SettingListItemType.OfficialSNS, url= null),
            SettingsItem(title="公式SNS", id= SettingListItemType.OfficialSNS, url= Url.OfficialSNS.urlString),
            SettingsItem(title="ソースコード", id= SettingListItemType.SourceCode, url= Url.SourceCode.urlString)
        )

        settingsItems = listOf(
            SettingsItem(title="パスワード", id= SettingListItemType.Password, url= null),
            SettingsItem(title="このアプリについて", id= SettingListItemType.AboutThisApp, url= Url.AppIntroduction.urlString),
            SettingsItem(title="お問い合わせ", id= SettingListItemType.ContactUs, url= Url.ContactUs.urlString),
            SettingsItem(title="公式SNS", id= SettingListItemType.OfficialSNS, url= Url.OfficialSNS.urlString),
            SettingsItem(title="ホームページ", id= SettingListItemType.HomePage, url= Url.HomePage.urlString),
            SettingsItem(title="利用規約", id= SettingListItemType.TermsOfService, url= Url.TermsOfService.urlString),
            SettingsItem(title="プライバシーポリシー", id= SettingListItemType.PrivacyPolicy, url= Url.PrivacyPolicy.urlString),
            SettingsItem(title="ソースコード", id= SettingListItemType.SourceCode, url= Url.SourceCode.urlString)
//            SettingsItem(title="レビュって", id= SettingListItemType., url= null),
//            SettingsItem(title="謝辞", id= SettingListItemType., url= null),
        )
    }
}