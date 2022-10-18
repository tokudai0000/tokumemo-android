package com.example.tokumemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.android.material.bottomnavigation.BottomNavigationView

class PasswordActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.home -> {
                finish()
            }
            R.id.others -> {

            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        initSetup()
    }

    private fun initSetup() {
        // findViewById
        val registerButton = findViewById<Button>(R.id.registerButton)

        val editCAccount = findViewById<EditText>(R.id.editCAccount)
        val cAccountLabel = findViewById<TextView>(R.id.cAccountLabel)
//        val cAccountMessageLabel = findViewById<TextView>(R.id.cAccountMessageLabel)

        val editPassword = findViewById<EditText>(R.id.editPassword)
        val passwordLabel = findViewById<TextView>(R.id.passwordLabel)
//        val passwordMessageLabel = findViewById<TextView>(R.id.passwordMessageLabel)

        val message = findViewById<TextView>(R.id.message)

        registerButton.setOnClickListener {
            val cAccountText = editCAccount.text.toString()
            val passwordText = editPassword.text.toString()

            message.text = "" // 初期値に戻す

            when {
                // 入力値が正常なデータか検証

                cAccountText.isEmpty() -> {
                    message.text = "Cアカウントが空欄です"
                }

                // cアカウントの先頭はcから始まる(isEmptyで検証してる為、エラーが起きない)
                cAccountText.substring(0, 1) != "c" -> {
                    message.text = "cアカウント例(c100100100)"
                }

                // cアカウントは10桁(よく@tokushima-u.ac.jpとつけるユーザーがいる為の対策)
                cAccountText.length > 10 -> {
                    message.text = "cアカウント例(c100100100)"
                }

                passwordText.isEmpty() -> {
                    message.text = "パスワードが空欄です"
                }

                else -> {
                    // 登録
                    encryptedSave("KEY_cAccount", cAccountText)
                    encryptedSave("KEY_password", passwordText)
                    // MainActivityで再ログインの処理を行わせる
                    val intent = Intent()
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }
            // パスワード未設定時のテスト用
//            encryptedSave("KEY_cAccount", cAccountText)
//            encryptedSave("KEY_password", passwordText)
//            // MainActivityで再ログインの処理を行わせる
//            val intent = Intent()
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }

        // 入力文字数のカウント、そして表示を行う
        editCAccount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            // テキストが変更された直後(入力が確定された後)に呼び出される
            override fun afterTextChanged(s: Editable?) {
                cAccountLabel.text = "${editCAccount.text.toString().length}/10"
            }
        })
        editPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                passwordLabel.text = "${editPassword.text.toString().length}/100"
            }
        })

        // 保存しているデータを入力フィールドに表示
        editCAccount.setText(encryptedLoad("KEY_cAccount"))
        editPassword.setText(encryptedLoad("KEY_password"))
    }


    // 以下、暗号化してデバイスに保存する(MainActivityにも存在するので今後、統一)
    companion object {
        const val PREF_NAME = "encrypted_prefs"
    }
    // 読み込み
    private fun encryptedLoad(KEY: String): String {
        val mainKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            applicationContext,
            PasswordActivity.PREF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        return prefs.getString(KEY, "")!! // nilの場合は空白を返す
    }
    // 保存
    private fun encryptedSave(KEY: String, text: String) {
        val mainKey = MasterKey.Builder(applicationContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        val prefs = EncryptedSharedPreferences.create(
            applicationContext,
            PasswordActivity.PREF_NAME,
            mainKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        with(prefs.edit()) {
            putString(KEY, text)
            apply()
        }
    }
}