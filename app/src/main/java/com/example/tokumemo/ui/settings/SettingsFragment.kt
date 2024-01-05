package com.example.tokumemo.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.tokumemo.R
import com.example.tokumemo.common.AppConstants
import com.example.tokumemo.domain.model.SettingsItem
import com.example.tokumemo.ui.web.WebActivity
import com.example.tokumemo.ui.password.PasswordActivity

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)

        val listView = view.findViewById<ListView>(R.id.settings_recycler_view)
        listView.adapter = SettingsListsAdapter(requireContext(), AppConstants.settingsItems)
        listView.setOnItemClickListener {_, _, position, _ ->
            val item = AppConstants.settingsItems[position]
            when (item.id) {
                SettingsItem.Type.Password -> {
                    val intent = Intent(requireContext(), PasswordActivity::class.java)
                    intent.putExtra("hogemon", PasswordActivity.DisplayType.Password)
                    startActivity(intent)
                }
                else -> {
                    val intent = Intent(requireContext(), WebActivity::class.java)
                    intent.putExtra("PAGE_KEY", item.targetUrl.toString())
                    startActivity(intent)
                }
            }
        }
        return view
    }
}