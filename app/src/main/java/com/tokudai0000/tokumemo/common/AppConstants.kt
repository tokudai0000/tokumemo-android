package com.tokudai0000.tokumemo.common

import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.domain.model.HomeMiniSettingsItem
import com.tokudai0000.tokumemo.domain.model.MenuDetailItem
import com.tokudai0000.tokumemo.domain.model.MenuItem
import com.tokudai0000.tokumemo.domain.model.SettingsItem

object AppConstants {
    val menuItems: List<MenuItem>
    val academicRelatedItems: List<MenuDetailItem>
    val libraryRelatedItems: List<MenuDetailItem>
    val etcItems: List<MenuDetailItem>
    val homeMiniSettingsItems: List<HomeMiniSettingsItem>
    val settingsItems: List<SettingsItem>

    init {
        menuItems = listOf(
            MenuItem(title="教務事務システム", id= MenuItem.Type.CourseManagement, image= R.drawable.coursemanagementhome, url= Url.CourseManagementMobile.urlString),
            MenuItem(title="manaba", id= MenuItem.Type.Manaba, image= R.drawable.manaba, url= Url.ManabaPC.urlString),
            MenuItem(title="メール", id= MenuItem.Type.Mail, image= R.drawable.mailservice, url= Url.OutlookService.urlString),
            MenuItem(title="講義関連", id= MenuItem.Type.AcademicRelated, image= R.drawable.coopcalendar, url=null),
            MenuItem(title="図書関連", id= MenuItem.Type.LibraryRelated, image= R.drawable.librarycalendar, url=null),
            MenuItem(title="その他", id= MenuItem.Type.Etc, image= R.drawable.careercenter, url=null),
        )

        academicRelatedItems = listOf(
            MenuDetailItem(title="時間割", id= MenuDetailItem.Type.TimeTable, targetUrl= Url.TimeTable.urlString),
            MenuDetailItem(title="今学期の成績", id= MenuDetailItem.Type.CurrentTermPerformance, targetUrl= Url.CurrentTermPerformance.urlString),
            MenuDetailItem(title="シラバス", id= MenuDetailItem.Type.Syllabus, targetUrl= Url.CurrentTermPerformance.urlString),
            MenuDetailItem(title="出欠記録", id= MenuDetailItem.Type.PresenceAbsenceRecord, targetUrl= Url.PresenceAbsenceRecord.urlString),
            MenuDetailItem(title="すべての成績", id= MenuDetailItem.Type.TermPerformance, targetUrl= Url.TermPerformance.urlString),
        )

        libraryRelatedItems = listOf(
            MenuDetailItem(title="常三島図書館 カレンダー", id= MenuDetailItem.Type.LibraryCalendarMain, targetUrl=Url.LibraryHomePageMainPC.urlString),
            MenuDetailItem(title="蔵本図書館 カレンダー", id= MenuDetailItem.Type.LibraryCalendarKura, targetUrl=Url.LibraryHomePageKuraPC.urlString),
            MenuDetailItem(title="貸出図書の期間延長", id= MenuDetailItem.Type.LibraryBookLendingExtension, targetUrl= Url.LibraryBookLendingExtension.urlString),
            MenuDetailItem(title="本の購入リクエスト", id= MenuDetailItem.Type.LibraryBookPurchaseRequest, targetUrl= Url.LibraryBookPurchaseRequest.urlString),
            MenuDetailItem(title="マイページ", id= MenuDetailItem.Type.LibraryMyPage, targetUrl= Url.LibraryMyPage.toString()),
        )

        etcItems = listOf(
            MenuDetailItem(title="生協営業時間割", id= MenuDetailItem.Type.CoopCalendar, targetUrl= Url.TokudaiCoop.urlString),
            MenuDetailItem(title="食堂メニュー", id= MenuDetailItem.Type.Cafeteria, targetUrl= Url.TokudaiCoopDinigMenu.urlString),
            MenuDetailItem(title="キャリア支援室", id= MenuDetailItem.Type.CareerCenter, targetUrl= Url.TokudaiCareerCenter.urlString),
            MenuDetailItem(title="SSS学習支援室の時間割", id= MenuDetailItem.Type.StudySupportSpace, targetUrl= Url.StudySupportSpace.urlString),
            MenuDetailItem(title="命を守る防災知識", id= MenuDetailItem.Type.DisasterPrevention, targetUrl= Url.DisasterPrevention.urlString),
        )

        homeMiniSettingsItems = listOf(
            HomeMiniSettingsItem(title="PR画像(広告)の利用申請", id= HomeMiniSettingsItem.Type.PrApplication, targetUrl= Url.PrApplication.urlString),
            HomeMiniSettingsItem(title="お問い合わせ", id= HomeMiniSettingsItem.Type.ContactUs, targetUrl= Url.ContactUs.urlString),
            HomeMiniSettingsItem(title="ホームページ", id= HomeMiniSettingsItem.Type.HomePage, targetUrl= Url.HomePage.urlString),
            HomeMiniSettingsItem(title="利用規約", id= HomeMiniSettingsItem.Type.TermsOfService, targetUrl= Url.TermsOfService.urlString),
            HomeMiniSettingsItem(title="プライバシーポリシー", id= HomeMiniSettingsItem.Type.PrivacyPolicy, targetUrl= Url.PrivacyPolicy.urlString),
//            HomeMiniSettingsItem(title="レビュって", id= HomeMiniSettingsItem.Type.OfficialSNS, targetUrl= null),
            HomeMiniSettingsItem(title="公式SNS", id= HomeMiniSettingsItem.Type.OfficialSNS, targetUrl= Url.OfficialSNS.urlString),
            HomeMiniSettingsItem(title="ソースコード", id= HomeMiniSettingsItem.Type.SourceCode, targetUrl= Url.SourceCode.urlString),
        )

        settingsItems = listOf(
            SettingsItem(title="パスワード", id= SettingsItem.Type.Password, targetUrl= null),
            SettingsItem(title="このアプリについて", id= SettingsItem.Type.AboutThisApp, targetUrl= Url.AppIntroduction.urlString),
            SettingsItem(title="ホームページ", id= SettingsItem.Type.HomePage, targetUrl= Url.HomePage.urlString),
            SettingsItem(title="お問い合わせ", id= SettingsItem.Type.ContactUs, targetUrl= Url.ContactUs.urlString),
            SettingsItem(title="公式SNS", id= SettingsItem.Type.OfficialSNS, targetUrl= Url.OfficialSNS.urlString),
            SettingsItem(title="利用規約", id= SettingsItem.Type.TermsOfService, targetUrl= Url.TermsOfService.urlString),
            SettingsItem(title="プライバシーポリシー", id= SettingsItem.Type.PrivacyPolicy, targetUrl= Url.PrivacyPolicy.urlString),
            SettingsItem(title="ソースコード", id= SettingsItem.Type.SourceCode, targetUrl= Url.SourceCode.urlString),
//            SettingsItem(title="レビュって", id= SettingsItem.Type., url= null),
//            SettingsItem(title="謝辞", id= SettingsItem.Type., url= null),
        )
    }
}