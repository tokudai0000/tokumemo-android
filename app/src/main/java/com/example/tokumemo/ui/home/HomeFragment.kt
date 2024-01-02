package com.example.tokumemo.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.utility.GetImage
import com.example.tokumemo.ui.password.PasswordActivity
import com.example.tokumemo.ui.pr.PublicRelationsActivity
import com.example.tokumemo.R
import com.example.tokumemo.domain.model.HomeListData
import com.example.tokumemo.domain.model.MenuListItemType
import com.example.tokumemo.ui.web.WebActivity
import com.example.tokumemo.utility.guard
import kotlinx.coroutines.*
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
        recyclerViewInitSetting(view)
        pRImagesInitSetting(view)

        return view
    }


    /// RecyclerViewの初期設定
    private fun recyclerViewInitSetting(view: View) {
        val displayMenuLists = viewModel.displayMenuList()
        val adapter = HomeMenuRecyclerAdapter(displayMenuLists)
        // 横3列に指定する
        menuRecyclerView.layoutManager = GridLayoutManager(context, 3, RecyclerView.VERTICAL, false)

        adapter.setOnBookCellClickListener(object :
            HomeMenuRecyclerAdapter.OnBookCellClickListener {
            override fun onItemClick(item: HomeListData) {
//                val cAccount = getPassword(view.context, "KEY_cAccount") ?: ""
//                val password = getPassword(view.context, "KEY_password") ?: ""
//                if ((cAccount.isEmpty() || password.isEmpty()) && item.isLockIconExists) {
//                    Toast.makeText(view?.context,
//                        "自動ログイン機能をONにしよう！Settingsから試してみてね",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }

                DataManager.canExecuteJavascript = true
                when(item.id) {
                    // 教務事務システム
                    MenuListItemType.CurrentTermPerformance -> {
                        // API24以下ではこれ API26以上ではLocalDate.now()が使用できる
                        val calendar = Calendar.getInstance()
                        var year = calendar.get(Calendar.YEAR)
                        val month = calendar.get(Calendar.MONTH) + 1 //月は0から始まるため、+1する

                        // 1月から3月までは前年度のURLを表示
                        if (month < 4){
                            year -= 1
                        }
                        val intent = Intent(requireContext(), WebActivity::class.java)
                        intent.putExtra("PAGE_KEY",item.url + year)
                        startActivity(intent)
                    }

                    // シラバス
                    MenuListItemType.Syllabus -> {
                        val intent = Intent(requireContext(), PasswordActivity::class.java)
                        intent.putExtra("hogemon", PasswordActivity.DisplayType.Syllabus)
                        startActivity(intent)
                    }

                    // 図書館カレンダー
                    MenuListItemType.LibraryCalendar -> {
                        val calendar = Calendar.getInstance()
                        val year = calendar.get(Calendar.YEAR)

                        // ダイアログの表示(常三島と蔵本を)
                        val alertDialog: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(requireContext(),
                            R.style.FirstDialogStyle
                        )
                        alertDialog.setTitle("図書館の所在を選択")
                        alertDialog.setMessage("こちらは最新情報ではありません。最新の情報は図書館ホームページをご覧ください。")
                        alertDialog.setNegativeButton("蔵本",
                            DialogInterface.OnClickListener { _, _ ->
                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_kura_$year.pdf&embedded=true"
                                val intent = Intent(requireContext(), WebActivity::class.java)
                                intent.putExtra("PAGE_KEY",libraryURL)
                                startActivity(intent)
                            })
                        alertDialog.setPositiveButton("常三島",
                            DialogInterface.OnClickListener { _, _ ->
                                val libraryURL = "https://docs.google.com/viewer?url=https://www.lib.tokushima-u.ac.jp/pub/pdf/calender/calender_main_$year.pdf&embedded=true"
                                val intent = Intent(requireContext(), WebActivity::class.java)
                                intent.putExtra("PAGE_KEY",libraryURL)
                                startActivity(intent)
                            })
                        alertDialog.show()
                    }

                    // 他のメニューはWebActivityで開く
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


    // 学生番号、パスワードを登録しているか判定
//    private fun hasRegisteredPassword(): Boolean {
//        val view = guard(view) { throw return false}
//        val cAccount = getPassword(view.context,"KEY_cAccount")
//        val password = getPassword(view.context,"KEY_password")
//
//        return !(cAccount == null || password == null)
//    }
}