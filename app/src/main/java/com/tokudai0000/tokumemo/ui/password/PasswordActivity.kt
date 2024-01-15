package com.tokudai0000.tokumemo.ui.password

import UnivAuthRepository
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.domain.model.UnivAuth

class PasswordActivity : AppCompatActivity() {

    private lateinit var finishButton: Button
    private lateinit var titleTextView: TextView
    private lateinit var titleLabel1: TextView
    private lateinit var titleLabel2: TextView
    private lateinit var textField1: EditText
    private lateinit var textField2: EditText
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        configInitSetup()
        configPasswordView()

        registerButton.setOnClickListener(){
            val univAuth = UnivAuth(textField1.text.toString(), textField2.text.toString())
            val repository = UnivAuthRepository(this)
            repository.setUnivAuth(univAuth)
            Toast.makeText(
                this,
                "保存完了！",
                Toast.LENGTH_SHORT
            ).show()
        }

        finishButton.setOnClickListener(){
            finish()
        }
    }

    private fun configInitSetup() {
        titleTextView = findViewById(R.id.urlText)
        titleLabel1 = findViewById(R.id.cAccountLabel)
        titleLabel2 = findViewById(R.id.passwordLabel)
        textField1 = findViewById(R.id.editStudentNumber)
        textField2 = findViewById(R.id.editPassword)
        registerButton = findViewById(R.id.registerButton)
        finishButton = findViewById(R.id.finish_button)
    }

    private fun configPasswordView() {
        titleTextView.text = "パスワード"
        titleLabel1.text = "cアカウント"
        titleLabel2.text = "パスワード"
        registerButton.text = "登録"

        // cアカウントをデバイス内から読み出す
        // パスワードについては、表示させないこと！！！
        val univAuthRepository = UnivAuthRepository(this)
        textField1.setText(univAuthRepository.fetchUnivAuth().accountCID)
    }
}