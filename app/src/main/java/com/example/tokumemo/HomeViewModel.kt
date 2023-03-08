package com.example.tokumemo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class HomeViewModel: ViewModel() {

    class PublicRelations
        (var imageURL:String?,
         var introduction:String?,
         var tappedURL:String?,
         var organization_name:String?,
         var description:String?)

    var prItems = arrayListOf<PublicRelations>()

    var displayPRImagesNumber: Int = -1 // 表示している広告がadItemsに入っている配列番号

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

    fun getPRItems(): Job = GlobalScope.launch {
        try {

            val jsonUrl = "https://tokudai0000.github.io/tokumemo_resource/pr_image/info.json"
            val str = URL(jsonUrl).readText()
            val json = JSONObject(str)

            // Jsonデータから内容物を取得
            val itemCounts = json.getInt("itemCounts")
            val items = json.getJSONArray("items")

            for (i in 0..itemCounts-1) {
                var item = items.getJSONObject(i)
                var prItem = PublicRelations(
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


    var initMenuList = listOf(
        MenuData(title="教務事務システム", id= MenuListItemType.courseManagementHomeMobile, image=R.drawable.coursemanagementhome, url= Url.courseManagementMobile.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="manaba", id= MenuListItemType.manabaHomePC, image=R.drawable.manaba, url= Url.manabaMobile.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="メール", id= MenuListItemType.mailService, image=R.drawable.mailservice, url= Url.outlookService.toString(), isLockIconExists=true, isHiddon=false),
        MenuData(title="[図書]本貸出延長", id= MenuListItemType.libraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url= Url.libraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="時間割", id= MenuListItemType.timeTable, image=R.drawable.timetable, url= Url.timeTable.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="今学期の成績", id= MenuListItemType.currentTermPerformance, image=R.drawable.currenttermperformance, url= Url.currentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="シラバス", id= MenuListItemType.syllabus, image=R.drawable.syllabus, url= Url.currentTermPerformance.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="生協カレンダー", id= MenuListItemType.coopCalendar, image=R.drawable.coopcalendar, url= Url.tokudaiCoop.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="今月の食堂メニュー", id= MenuListItemType.cafeteria, image=R.drawable.cafeteria, url= Url.tokudaiCoopDinigMenu.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]カレンダー", id= MenuListItemType.libraryCalendar, image=R.drawable.librarycalendar, url=null, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]本検索", id= MenuListItemType.libraryBookLendingExtension, image=R.drawable.librarybooklendingextension, url= Url.libraryBookLendingExtension.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="キャリア支援室", id= MenuListItemType.careerCenter, image=R.drawable.careercenter, url= Url.tokudaiCareerCenter.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="[図書]本購入", id= MenuListItemType.libraryBookPurchaseRequest, image=R.drawable.librarybookpurchaserequest, url= Url.libraryBookPurchaseRequest.urlString, isLockIconExists=true, isHiddon=false),
        MenuData(title="SSS時間割", id= MenuListItemType.studySupportSpace, image=R.drawable.studysupportspace, url= Url.studySupportSpace.urlString, isLockIconExists=false, isHiddon=false),
        MenuData(title="知っておきたい防災", id= MenuListItemType.disasterPrevention, image=R.drawable.disasterprevention, url= Url.disasterPrevention.urlString, isLockIconExists=true, isHiddon=false),

        // Hiddon
        MenuData(title="統合認証ポータル", id= MenuListItemType.portal, image=R.drawable.coursemanagementhome, url= Url.portal.urlString, isLockIconExists=false, isHiddon=true),
        MenuData(title="全学期の成績", id= MenuListItemType.termPerformance, image=R.drawable.currenttermperformance, url= Url.termPerformance.urlString, isLockIconExists=true, isHiddon=true),
        MenuData(title="大学サイト", id= MenuListItemType.universityWeb, image=R.drawable.coursemanagementhome, url= Url.universityHomePage.urlString, isLockIconExists=false, isHiddon=true),
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