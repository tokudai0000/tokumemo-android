package com.example.tokumemo.main.home

import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.ViewModel
import com.example.tokumemo.R
import com.example.tokumemo.model.Url

class HomeViewModel: ViewModel() {
    var initMenuList = listOf(
        MenuData(title="教務事務システム", id=MenuListItemType.courseManagementHomeMobile, image=R.drawable.coursemanagementhome, url=Url.courseManagementMobile.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="manaba", id=MenuListItemType.manabaHomePC, image=R.drawable.manaba, url=Url.manabaMobile.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="メール", id=MenuListItemType.mailService, image=R.drawable.mailservice, url=Url.outlookService.toString(), isLockIconExists=true, isHiddon=false),
        MenuData(title="[図書]本貸出延長", id=MenuListItemType.libraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url=Url.libraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="時間割", id=MenuListItemType.timeTable, image=R.drawable.timetable, url=Url.timeTable.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="今学期の成績", id=MenuListItemType.currentTermPerformance, image=R.drawable.currenttermperformance, url=Url.currentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="シラバス", id=MenuListItemType.syllabus, image=R.drawable.syllabus, url=Url.currentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="生協カレンダー", id=MenuListItemType.coopCalendar, image=R.drawable.coopcalendar, url=Url.tokudaiCoop.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="今月の食堂メニュー", id=MenuListItemType.cafeteria, image=R.drawable.cafeteria, url=Url.tokudaiCoopDinigMenu.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]カレンダー", id=MenuListItemType.libraryCalendar, image=R.drawable.librarycalendar, url=null, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]本検索", id=MenuListItemType.libraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url=Url.libraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="キャリア支援室", id=MenuListItemType.careerCenter, image=R.drawable.careercenter, url=Url.tokudaiCareerCenter.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]本購入", id=MenuListItemType.libraryBookPurchaseRequest, image=R.drawable.librarybookpurchaserequest, url=Url.libraryBookPurchaseRequest.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="SSS時間割", id=MenuListItemType.studySupportSpace, image=R.drawable.studysupportspace, url=Url.studySupportSpace.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="知っておきたい防災", id=MenuListItemType.disasterPrevention, image=R.drawable.disasterprevention, url=Url.disasterPrevention.urlString, isLockIconExists=true, isHiddon=false),

        // Hiddon
        MenuData(title="統合認証ポータル", id=MenuListItemType.portal, image=R.drawable.coursemanagementhome, url=Url.portal.urlString, isLockIconExists=false, isHiddon=true),
        MenuData(title="全学期の成績", id=MenuListItemType.termPerformance, image=R.drawable.currenttermperformance, url=Url.termPerformance.urlString, isLockIconExists=true, isHiddon=true),
        MenuData(title="大学サイト", id=MenuListItemType.universityWeb, image=R.drawable.coursemanagementhome, url=Url.universityHomePage.urlString, isLockIconExists=false, isHiddon=true),
//        MenuData(title="教務システム_PC", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=true, isHiddon=true),
//        MenuData(title="manaba_Mob", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=true, isHiddon=true),
//        MenuData(title="図書館サイト", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=false, isHiddon=true),
//        MenuData(title="出欠記録", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=true, isHiddon=true),
//        MenuData(title="授業アンケート", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=true, isHiddon=true),
//        MenuData(title="LMS一覧", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=false, isHiddon=true),
//        MenuData(title="[図書]HP_常三島", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=false, isHiddon=true),
//        MenuData(title="[図書]HP_蔵本", id=MenuListItemType, image=R.drawable, url=Url, isLockIconExists=false, isHiddon=true)
    )
}