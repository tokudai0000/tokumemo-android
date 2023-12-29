package com.example.tokumemo.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tokumemo.R
import com.example.tokumemo.ui.web.WebActivity
import com.example.tokumemo.ui.password.PasswordActivity

class SettingsFragment : Fragment() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]


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
                SettingListItemType.Favorite -> {
                    Toast.makeText(
                        view.context,
                        "近日中に実装",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                SettingListItemType.Customize -> {
                    Toast.makeText(
                        view.context,
                        "近日中に実装",
                        Toast.LENGTH_SHORT
                    ).show()
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