package com.tokudai0000.tokumemo.ui.main.fragment.home

import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Space
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.common.AKLog
import com.tokudai0000.tokumemo.common.AKLogLevel
import com.tokudai0000.tokumemo.common.AppConstants
import com.tokudai0000.tokumemo.common.CustomDuoButton
import com.tokudai0000.tokumemo.common.UrlCheckers
import com.tokudai0000.tokumemo.domain.model.MenuDetailItem
import com.tokudai0000.tokumemo.domain.model.MenuItem
import com.tokudai0000.tokumemo.ui.pr.PublicRelationsActivity
import com.tokudai0000.tokumemo.ui.web.WebActivity
import com.tokudai0000.tokumemo.utility.GetImage


class HomeFragment : Fragment() {

    private lateinit var view: View
    private lateinit var menuRecyclerView: RecyclerView

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        view = inflater.inflate(R.layout.fragment_home, container, false)

        configureNumberOfUsers()
        configureAdImages()
        configureMenuRecyclerView()
        configureHomeMiniSettingsListView()
        configureHomeEventInfos()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun configureNumberOfUsers() {
        val numberOfUsersTextView: TextView = view.findViewById(R.id.number_of_users_text_view)
        viewModel.numberOfUsers.observe(viewLifecycleOwner) { numberOfUsers ->
            numberOfUsersTextView.text = numberOfUsers
        }
        viewModel.getNumberOfUsers()
    }

    private fun configureAdImages() {
        val prImageView = view.findViewById<ImageView>(R.id.pr_image_button)
        val univImageView = view.findViewById<ImageView>(R.id.univ_image_button)

        viewModel.displayPrItem.observe(viewLifecycleOwner) { adItem ->
            GetImage(prImageView).execute(adItem.imageUrlStr)
        }
        viewModel.displayUnivItem.observe(viewLifecycleOwner) { adItem ->
            GetImage(univImageView).execute(adItem.imageUrlStr)
        }
        // PR画像(広告)の取得
        viewModel.getAdItems()

        prImageView.setOnClickListener {
            viewModel.displayPrItem.value?.let { item ->
                // PRActivityを表示
                val intent = Intent(requireContext(), PublicRelationsActivity::class.java)
                intent.putExtra(WebActivity.KEY_URL, item)
                startActivity(intent)
            }
        }

        univImageView.setOnClickListener {
            viewModel.displayUnivItem.value?.let { item ->
                // WebActivityを表示
                val intent = Intent(requireContext(), WebActivity::class.java)
                intent.putExtra(WebActivity.KEY_URL, item.targetUrlStr)
                startActivity(intent)
            }
        }

        // 広告を5000 msごとに読み込ませる
        Timer().scheduleAtFixedRate(0, 5000) {
            viewModel.randomChoiceForAdImage(
                adItems = viewModel.prItems,
                displayAdItem = viewModel.displayPrItem.value
            )?.let {
                viewModel.displayPrItem.postValue(it)
            }
            viewModel.randomChoiceForAdImage(
                adItems = viewModel.univItems,
                displayAdItem = viewModel.displayUnivItem.value
            )?.let {
                viewModel.displayUnivItem.postValue(it)
            }
        }
    }


    /// RecyclerViewの初期設定
    private fun configureMenuRecyclerView() {
        menuRecyclerView = view.findViewById<RecyclerView>(R.id.menu_recycler_view)
        val displayMenuLists = AppConstants.menuItems
        val adapter = HomeMenuRecyclerAdapter(displayMenuLists)
        // 横3列に指定する
        menuRecyclerView.layoutManager = NoScrollGridLayoutManager(requireContext(), 3)

        adapter.setOnBookCellClickListener(object :
            HomeMenuRecyclerAdapter.OnBookCellClickListener {
            override fun onItemClick(item: MenuItem) {

                when (item.id) {

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
                        intent.putExtra(WebActivity.KEY_URL, item.url)
                        startActivity(intent)
                    }
                }
            }
        })
        menuRecyclerView.adapter = adapter
    }

    // メニュー画面において、講義関連、図書館関連、その他でポップアップを表示させる
    private fun showMenuDetailDialog(items: List<MenuDetailItem>) {
        val builder = AlertDialog.Builder(requireContext())

        // MenuDetailItem の配列から表示用の文字列の配列を生成
        val itemNames = items.map { it.title }.toTypedArray()

        builder.setItems(itemNames) { dialog, which ->
            // 図書館カレンダー.pdfのURLをWebスクレイピングしてくる
            if (items[which].id == MenuDetailItem.Type.LibraryCalendarMain ||
                items[which].id == MenuDetailItem.Type.LibraryCalendarKura ) {
                items[which].targetUrl?.let {
                    viewModel.getLibraryCalendarURL(it)
                    viewModel.libraryCalendarURL.observe(viewLifecycleOwner) { urlStr ->
                        val intent = Intent(requireContext(), WebActivity::class.java)
                        val url = UrlCheckers.convertToGoogleDocsViewerUrlIfNeeded(urlStr)
                        intent.putExtra(WebActivity.KEY_URL, url)
                        startActivity(intent)
                        AKLog(AKLogLevel.DEBUG, "URL - $url")
                    }
                }
            }else{
                val intent = Intent(requireContext(), WebActivity::class.java)
                intent.putExtra(WebActivity.KEY_URL, items[which].targetUrl)
                startActivity(intent)
            }
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun configureHomeMiniSettingsListView() {
        val homeMiniSettingsRecyclerView =
            view.findViewById<ListView>(R.id.home_mini_settings_recycler_view)
        val displayMenuLists = AppConstants.homeMiniSettingsItems
        homeMiniSettingsRecyclerView.adapter =
            HomeMiniSettingsRecyclerAdapter(requireContext(), displayMenuLists)
        homeMiniSettingsRecyclerView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(requireContext(), WebActivity::class.java)
            intent.putExtra(WebActivity.KEY_URL, displayMenuLists[position].targetUrl.toString())
            startActivity(intent)
        }
    }

    private fun configureHomeEventInfos() {
        viewModel.getHomeEventInfos()

        viewModel.buttonItems.observe(viewLifecycleOwner, { buttonItems ->
            for ((index, item) in buttonItems.withIndex()) {
                setupCustomDuoButton(view, title = item.titleName, tag = index)
            }
        })
    }

    private fun setupCustomDuoButton(view: View, title: String, tag: Int) {
        val customButton = CustomDuoButton(requireContext()).apply {
            setupButton(title = title, tag = tag) // 他のパラメータも適宜設定
            onTap = { tag ->
                val intent = Intent(requireContext(), WebActivity::class.java)
                viewModel.buttonItems.value?.let { list ->
                    if (tag <= list.size) {
                        val url = list[tag].targetUrlStr
                        intent.putExtra(WebActivity.KEY_URL, url)
                        startActivity(intent)
                    }else{
                        AKLog(AKLogLevel.ERROR, "setupCustomDuoButtonにおける、List数エラー")
                    }
                }
            }
        }

        val layoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        ).apply {}
        customButton.layoutParams = layoutParams

        val buttonContainer = view.findViewById<LinearLayout>(R.id.event_button_container)
        buttonContainer.addView(customButton)
    }
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