package com.example.tokumemo.ui.home

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.R
import com.example.tokumemo.common.AppConstants
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.domain.model.HomeMiniSettingsItem
import com.example.tokumemo.domain.model.MenuDetailItem
import com.example.tokumemo.domain.model.MenuItem
import com.example.tokumemo.domain.model.SettingsItem
import com.example.tokumemo.ui.password.PasswordActivity
import com.example.tokumemo.ui.pr.PublicRelationsActivity
import com.example.tokumemo.ui.settings.SettingsListsAdapter
import com.example.tokumemo.ui.web.WebActivity
import com.example.tokumemo.utility.GetImage
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate


class HomeFragment : Fragment() {

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var menuRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        menuRecyclerView = view.findViewById<RecyclerView>(R.id.menu_recycler_view)
        // PR画像(広告)の取得
        viewModel.getAdItems()
        menuRecyclerViewInitSetting(view)
        pRImagesInitSetting(view)
        homeMiniSettingsRecyclerViewInitSetting(view)

        return view
    }


    /// RecyclerViewの初期設定
    private fun menuRecyclerViewInitSetting(view: View) {
        val displayMenuLists = AppConstants.menuItems
        val adapter = HomeMenuRecyclerAdapter(displayMenuLists)
        // 横3列に指定する
        menuRecyclerView.layoutManager = NoScrollGridLayoutManager(requireContext(), 3)

        adapter.setOnBookCellClickListener(object :
            HomeMenuRecyclerAdapter.OnBookCellClickListener {
            override fun onItemClick(item: MenuItem) {

                DataManager.canExecuteJavascript = true
                when(item.id) {

                    // 講義関連
                    MenuItem.Type.AcademicRelated -> {
                        showMenuDetailDialog(AppConstants.academicRelatedItems)
                    }

                    // 図書館関連
                    MenuItem.Type.LibraryRelated -> {
                        showMenuDetailDialog(AppConstants.libraryRelatedItems)
                    }

                    // その他
                    MenuItem.Type.Etc -> {
                        showMenuDetailDialog(AppConstants.etcItems)
                    }

                    else -> {
                        val intent = Intent(requireContext(), WebActivity::class.java)
                        intent.putExtra("PAGE_KEY",item.url)
                        startActivity(intent)
                    }
                }
            }
        })
        menuRecyclerView.adapter = adapter
    }

    private fun homeMiniSettingsRecyclerViewInitSetting(view: View) {
        val homeMiniSettingsRecyclerView = view.findViewById<ListView>(R.id.home_mini_settings_recycler_view)
        val displayMenuLists = AppConstants.homeMiniSettingsItems
        homeMiniSettingsRecyclerView.adapter = HomeMiniSettingsRecyclerAdapter(requireContext(), displayMenuLists)
        homeMiniSettingsRecyclerView.setOnItemClickListener {_, _, position, _ ->
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("PAGE_KEY", displayMenuLists[position].targetUrl.toString())
            startActivity(intent)
        }
    }

    /// PR画像についての初期設定
    private fun pRImagesInitSetting(view: View) {
        val imageView = view.findViewById<ImageView>(R.id.pr_image_button)
        imageView.setOnClickListener {
            // PR画像が表示されているのならdisplayPRImagesNumberには値が入っている
            viewModel.displayPRImagesNumber?.let {
                // 表示されているPR画像の情報をPublicRelationsActivityに値を渡す
                viewModel.prItems[it].let {
                    val intent = Intent(context, PublicRelationsActivity::class.java)
                    intent.putExtra("PR_imageURL",it.imageURL)
                    intent.putExtra("PR_introduction",it.introduction)
                    intent.putExtra("PR_description",it.description)
                    intent.putExtra("PR_tappedURL",it.tappedURL)
                    intent.putExtra("PR_organization_name",it.organization_name)
                    startActivity(intent)
                }
            }
        }

        // PR画像(広告)を10000 msごとに読み込ませる
        Timer().scheduleAtFixedRate(0, 5000) {
            viewModel.selectPRImageNumber()?.let {
                viewModel.displayPRImagesNumber = it
                GetImage(imageView).execute(viewModel.prItems[it].imageURL)
            }
        }
    }

    private fun showMenuDetailDialog(items: List<MenuDetailItem>) {
        val builder = AlertDialog.Builder(requireContext())

        // MenuDetailItem の配列から表示用の文字列の配列を生成
        val itemNames = items.map { it.title }.toTypedArray()

        builder.setItems(itemNames) { dialog, which ->
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra("PAGE_KEY",items[which].targetUrl)
            startActivity(intent)
        }

        val dialog = builder.create()
        dialog.show()
    }


    // 学生番号、パスワードを登録しているか判定
//    private fun hasRegisteredPassword(): Boolean {
//        val view = guard(view) { throw return false}
//        val cAccount = getPassword(view.context,"KEY_cAccount")
//        val password = getPassword(view.context,"KEY_password")
//
//        return !(cAccount == null || password == null)
//    }
    //                    // 図書館カレンダー
//                    MenuListItemType.LibraryCalendar -> {
//                        val calendar = Calendar.getInstance()
//                        val year = calendar.get(Calendar.YEAR)
//
//                        // ダイアログの表示(常三島と蔵本を)
//                        val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext(),
//                            R.style.FirstDialogStyle
//                        )
//                        alertDialog.setTitle("図書館の所在を選択")
//                        alertDialog.setMessage("こちらは最新情報ではありません。最新の情報は図書館ホームページをご覧ください。")
//                        alertDialog.setNegativeButton("蔵本",
//                            DialogInterface.OnClickListener { _, _ ->
//                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_$year.pdf&embedded=true"
//                                val intent = Intent(requireContext(), WebActivity::class.java)
//                                intent.putExtra("PAGE_KEY",libraryURL)
//                                startActivity(intent)
//                            })
//                        alertDialog.setPositiveButton("常三島",
//                            DialogInterface.OnClickListener { _, _ ->
//                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_$year.pdf&embedded=true"
//                                val intent = Intent(requireContext(), WebActivity::class.java)
//                                intent.putExtra("PAGE_KEY",libraryURL)
//                                startActivity(intent)
//                            })
//                        alertDialog.show()
//                    }
}
class NoScrollGridLayoutManager(context: Context, spanCount: Int) : GridLayoutManager(context, spanCount) {
    override fun canScrollVertically(): Boolean {
        // 縦方向のスクロールを禁止
        return false
    }

    override fun canScrollHorizontally(): Boolean {
        // 横方向のスクロールを禁止
        return false
    }
}