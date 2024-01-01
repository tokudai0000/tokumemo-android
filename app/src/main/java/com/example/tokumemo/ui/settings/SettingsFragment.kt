package com.example.tokumemo.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.tokumemo.R
import com.example.tokumemo.ui.web.WebActivity
import com.example.tokumemo.ui.password.PasswordActivity

class SettingsFragment : Fragment() {

    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        // xmlにて実装したListViewの取得
        val listView = view.findViewById<ListView>(R.id.settings_recycler_view)
        listView.adapter = SettingsListsAdapter(requireContext(), viewModel.initSettingsList)
        listView.setOnItemClickListener {_, view, position, _ ->
            val item = viewModel.initSettingsList[position]
            when (item.id) {
                SettingListItemType.Password -> {
                    val intent = Intent(requireContext(), PasswordActivity::class.java)
                    intent.putExtra("hogemon", PasswordActivity.DisplayType.Password)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(requireContext(), WebActivity::class.java)
                    intent.putExtra("PAGE_KEY", item.url.toString())
                    startActivity(intent)
                }
            }
        }
        return view
    }
}