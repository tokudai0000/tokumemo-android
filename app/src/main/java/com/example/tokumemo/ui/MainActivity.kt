package com.example.tokumemo.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.tokumemo.ui.agreement.AgreementActivity
import com.example.tokumemo.data.DataManager
import com.example.tokumemo.R
import com.example.tokumemo.ui.splash.SplashActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {

        const val EXTRA_RESULT = "result"

        fun createIntent(context: Context) =
            Intent(context, MainActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        print("🖐️　あああ")
    }
}