package com.example.tokumemo

import FirstDialogFragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
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
}