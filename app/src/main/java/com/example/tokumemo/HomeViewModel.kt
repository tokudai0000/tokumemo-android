package com.example.tokumemo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class HomeViewModel: ViewModel() {

    var prItems = arrayListOf<PublicRelationsData>()

    var displayPRImagesNumber: Int? = null // 表示している広告がadItemsに入っている配列番号

    fun getPRItemsFromGithub(): Job = GlobalScope.launch {
        try {

            val jsonUrl = "https://tokudai0000.github.io/tokumemo_resource/pr_image/info.json"
            val str = URL(jsonUrl).readText()
            val json = JSONObject(str)

            // Jsonデータから内容物を取得
            val itemCounts = json.getInt("itemCounts")
            val items = json.getJSONArray("items")

            for (i in 0..itemCounts-1) {
                var item = items.getJSONObject(i)
                var prItem = PublicRelationsData(
                    imageURL = item.getString("imageURL"),
                    introduction = item.getString("introduction"),
                    tappedURL = item.getString("tappedURL"),
                    organization_name = item.getString("organization_name"),
                    description = item.getString("description")
                )
                prItems.add(prItem)
            }

        } catch (e: Exception) {
            // Error
        }
    }
    
    fun selectPRImageNumber(): Int? {
        // 広告数が0か1の場合はローテーションする必要がない
        if (prItems.count() == 0) {
            return null
        } else if (prItems.count() == 1) {
            return 0
        }

        while (true) {
            val randomNum = kotlin.random.Random.nextInt(0, prItems.count())
            // 前回の画像表示番号と同じであれば、再度繰り返す
            if (randomNum != displayPRImagesNumber) {
                return randomNum
            }
        }

    }


    fun displayMenuList(): List<HomeListData> {
        var displayLists = mutableListOf<HomeListData>()
        for(item in initMenuList) {
            if(!item.isHiddon) {
                displayLists.add(item)
            }
        }
        return displayLists
    }


    var initMenuList = listOf(
        HomeListData(title="教務事務システム", id= MenuListItemType.CourseManagementHomeMobile, image=R.drawable.coursemanagementhome, url= Url.CourseManagementMobile.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="manaba", id= MenuListItemType.ManabaHomePC, image=R.drawable.manaba, url= Url.ManabaMobile.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="メール", id= MenuListItemType.MailService, image=R.drawable.mailservice, url= Url.OutlookService.toString(), isLockIconExists=true, isHiddon=false),
        HomeListData(title="[図書]本貸出延長", id= MenuListItemType.LibraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url= Url.LibraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="時間割", id= MenuListItemType.TimeTable, image=R.drawable.timetable, url= Url.TimeTable.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="今学期の成績", id= MenuListItemType.CurrentTermPerformance, image=R.drawable.currenttermperformance, url= Url.CurrentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="シラバス", id= MenuListItemType.Syllabus, image=R.drawable.syllabus, url= Url.CurrentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="生協カレンダー", id= MenuListItemType.CoopCalendar, image=R.drawable.coopcalendar, url= Url.TokudaiCoop.urlString, isLockIconExists=false, isHiddon=false),
        HomeListData(title="今月の食堂メニュー", id= MenuListItemType.Cafeteria, image=R.drawable.cafeteria, url= Url.TokudaiCoopDinigMenu.urlString, isLockIconExists=false, isHiddon=false),
        HomeListData(title="[図書]カレンダー", id= MenuListItemType.LibraryCalendar, image=R.drawable.librarycalendar, url=null, isLockIconExists=false, isHiddon=false),
        HomeListData(title="[図書]本検索", id= MenuListItemType.LibraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url= Url.LibraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="キャリア支援室", id= MenuListItemType.CareerCenter, image=R.drawable.careercenter, url= Url.TokudaiCareerCenter.urlString, isLockIconExists=false, isHiddon=false),
        HomeListData(title="[図書]本購入", id= MenuListItemType.LibraryBookPurchaseRequest, image=R.drawable.librarybookpurchaserequest, url= Url.LibraryBookPurchaseRequest.urlString, isLockIconExists=true, isHiddon=false),
        HomeListData(title="SSS時間割", id= MenuListItemType.StudySupportSpace, image=R.drawable.studysupportspace, url= Url.StudySupportSpace.urlString, isLockIconExists=false, isHiddon=false),
        HomeListData(title="知っておきたい防災", id= MenuListItemType.DisasterPrevention, image=R.drawable.disasterprevention, url= Url.DisasterPrevention.urlString, isLockIconExists=true, isHiddon=false),

        // Hiddon
        HomeListData(title="統合認証ポータル", id= MenuListItemType.Portal, image=R.drawable.coursemanagementhome, url= Url.Portal.urlString, isLockIconExists=false, isHiddon=true),
        HomeListData(title="全学期の成績", id= MenuListItemType.TermPerformance, image=R.drawable.currenttermperformance, url= Url.TermPerformance.urlString, isLockIconExists=true, isHiddon=true),
        HomeListData(title="大学サイト", id= MenuListItemType.UniversityWeb, image=R.drawable.coursemanagementhome, url= Url.UniversityHomePage.urlString, isLockIconExists=false, isHiddon=true),
        HomeListData(title="教務システム_PC", id=MenuListItemType.CourseManagementHomePC, image=R.drawable.coursemanagementhome, url=Url.CourseManagementPC.urlString, isLockIconExists=true, isHiddon=true),
        HomeListData(title="manaba_Mob", id=MenuListItemType.ManabaHomeMobile, image=R.drawable.manaba, url=Url.ManabaMobile.urlString, isLockIconExists=true, isHiddon=true),
        HomeListData(title="図書館サイト", id=MenuListItemType.LibraryMyPage, image=R.drawable.librarybooklendingextension, url=Url.LibraryMyPage.urlString, isLockIconExists=false, isHiddon=true),
        HomeListData(title="出欠記録", id=MenuListItemType.PresenceAbsenceRecord, image=R.drawable.coursemanagementhome, url=Url.PresenceAbsenceRecord.urlString, isLockIconExists=true, isHiddon=true),
        HomeListData(title="授業アンケート", id=MenuListItemType.ClassQuestionnaire, image=R.drawable.coursemanagementhome, url=Url.ClassQuestionnaire.urlString, isLockIconExists=true, isHiddon=true),
        HomeListData(title="LMS一覧", id=MenuListItemType.ELearningList, image=R.drawable.manaba, url=Url.ELearningList.urlString, isLockIconExists=false, isHiddon=true),
        HomeListData(title="[図書]HP_常三島", id=MenuListItemType.LibraryWebHomePC, image=R.drawable.librarybooklendingextension, url=Url.LibraryHomePageMainPC.urlString, isLockIconExists=false, isHiddon=true),
        HomeListData(title="[図書]HP_蔵本", id=MenuListItemType.LibraryWebHomeKuraPC, image=R.drawable.librarybooklendingextension, url=Url.LibraryHomePageKuraPC.urlString, isLockIconExists=false, isHiddon=true)
    )
}