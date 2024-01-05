package com.example.tokumemo.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tokumemo.R
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.ui.agreement.AgreementActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        configureLoginStatus()
        configureCopylight()

    }

    private fun configureLoginStatus() {
        val loginStatus: TextView = findViewById(R.id.loginStatusTextView)
        loginStatus.text = "認証確認中"
        loginStatus.gravity = Gravity.CENTER
        loginStatus.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }

    private fun configureCopylight() {
        val copylight: TextView = findViewById(R.id.copylightTextView)
        copylight.text = "Developed by Tokushima Univ students \n GitHub @tokudai0000"
        copylight.gravity = Gravity.CENTER
        copylight.setTextColor(Color.LTGRAY)
        copylight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
    }
}