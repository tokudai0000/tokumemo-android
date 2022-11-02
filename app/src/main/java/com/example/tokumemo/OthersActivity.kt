package com.example.tokumemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.tokumemo.manager.DataManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class OthersActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.news -> {

            }
            R.id.review -> {

            }
            R.id.others -> {

            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)

        // メニューバー表示
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
        navView.setOnItemSelectedListener(onNavigationItemSelectedListener)

        // パスワード設定を押したとき
        val passwordSetting = findViewById<Button>(R.id.passwordSetting)
        passwordSetting.setOnClickListener{
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }
    }
}