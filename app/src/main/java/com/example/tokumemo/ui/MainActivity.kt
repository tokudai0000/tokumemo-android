package com.example.tokumemo.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.tokumemo.ui.agreement.AgreementActivity
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)
        setupWithNavController(bottomNavigation, navController)

        // 初回起動時に利用規約ダイアログを表示
        val KEY = "KEY_agreementVersion"
        val newAgreementVer = DataManager.agreementVer
        val oldAgreementVer = getSharedPreferences("my_settings", Context.MODE_PRIVATE).getString(KEY, null).toString()
        if ( newAgreementVer != oldAgreementVer) {
            val intent = Intent(this, AgreementActivity::class.java)
            startActivity(intent)
        }
    }
    /// 最新の利用規約同意者か判定し、同意画面の表示を行うべきか判定
//    private fun shouldShowTermsAgreementView(): Boolean {
//        return (DataManager.agreementVersion != ConstStruct.latestTermsVersion)
//    }
}