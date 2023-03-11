package com.example.tokumemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PasswordActivity : AppCompatActivity() {

    lateinit var finishButton: Button
    lateinit var titleTextView: TextView
    lateinit var titleLabel1: TextView
    lateinit var titleLabel2: TextView
    lateinit var textField1: EditText
    lateinit var textField2: EditText
    lateinit var registerButton: Button

    enum class DisplayType {
        Password,
        Favorite,
        Syllabus
    }
//    public lateinit var type: DisplayType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        finishButton = findViewById(R.id.finish_button)
        titleTextView = findViewById(R.id.titleText)
        titleLabel1 = findViewById(R.id.cAccountLabel)
        titleLabel2 = findViewById(R.id.passwordLabel)
        textField1 = findViewById(R.id.editStudentNumber)
        textField2 = findViewById(R.id.editPassword)
        registerButton = findViewById(R.id.registerButton)

        val type: DisplayType = intent.getSerializableExtra("hogemon") as DisplayType

        when(type) {
            DisplayType.Password ->{
                titleTextView.text = "パスワード"
                titleLabel1.text = "cアカウント"
                titleLabel2.text = "パスワード"
                registerButton.text = "登録"
            }

            DisplayType.Favorite -> {
            }

            DisplayType.Syllabus -> {
                titleTextView.text = "シラバス"
                titleLabel1.text = "教員名"
                titleLabel2.text = "科目名"
                registerButton.text = "検索"
            }
        }

        finishButton.setOnClickListener(){
            finish()
        }
    }

//    private fun initSetup() {
//        // findViewById
//        val registerButton = findViewById<Button>(R.id.registerButton)
//
//        val editStudentNumber = findViewById<EditText>(R.id.editStudentNumber)
//        val cAccountLabel = findViewById<TextView>(R.id.cAccountLabel)
////        val cAccountMessageLabel = findViewById<TextView>(R.id.cAccountMessageLabel)
//
//        val editPassword = findViewById<EditText>(R.id.editPassword)
//        val passwordLabel = findViewById<TextView>(R.id.passwordLabel)
////        val passwordMessageLabel = findViewById<TextView>(R.id.passwordMessageLabel)
//
////        val message = findViewById<TextView>(R.id.message)
//
//        registerButton.setOnClickListener {
//            val studentNumber = editStudentNumber.text.toString()
//            val passwordText = editPassword.text.toString()
//
////            message.text = "" // 初期値に戻す
//
//            when {
//                // 入力値が正常なデータか検証
//
//                studentNumber.isEmpty() -> {
////                    message.text = "学生番号が空欄です"
//                }
//
//                // cアカウントはエラー
//                studentNumber.substring(0, 1) == "c" -> {
////                    message.text = "学籍番号を入力してください(例：1234567890)"
//                }
//
////                // 桁数（先生は9桁）
////                studentNumber.length != 9 && studentNumber.length != 10 -> {
////                    message.text = "桁数が正しくありません"
////                }
//
//                passwordText.isEmpty() -> {
////                    message.text = "パスワードが空欄です"
//                }
//
//                else -> {
//                    // 登録
//                    var cAccount = ""
//                    cAccount = if (studentNumber.length == 10) {
//                        "c" + studentNumber.dropLast(1)
//                    }else {
//                        "c$studentNumber"
//                    }
//                    encryptedSave("KEY_cAccount", cAccount)
//                    encryptedSave("KEY_studentNumber", studentNumber)
//                    encryptedSave("KEY_password", passwordText)
//                    // MainActivityで再ログインの処理を行わせる
//                    val intent = Intent()
//                    setResult(Activity.RESULT_OK, intent)
//                    finish()
//                }
//            }
//            // パスワード未設定時のテスト用
////            encryptedSave("KEY_cAccount", studentNumber)
////            encryptedSave("KEY_password", passwordText)
////            // MainActivityで再ログインの処理を行わせる
////            val intent = Intent()
////            setResult(Activity.RESULT_OK, intent)
////            finish()
//        }
//
//        // 入力文字数のカウント、そして表示を行う
//        editStudentNumber.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//
//            // テキストが変更された直後(入力が確定された後)に呼び出される
//            override fun afterTextChanged(s: Editable?) {
//                cAccountLabel.text = "学籍番号　${editStudentNumber.text.toString().length}/10"
//            }
//        })
//        editPassword.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
//            override fun afterTextChanged(s: Editable?) {
//                passwordLabel.text = "パスワード　${editPassword.text.toString().length}/100"
//            }
//        })
//
//        // 保存しているデータを入力フィールドに表示
//        editStudentNumber.setText(encryptedLoad("KEY_studentNumber"))
//        editPassword.setText(encryptedLoad("KEY_password"))
//    }
//
//
//    // 以下、暗号化してデバイスに保存する(MainActivityにも存在するので今後、統一)
//    companion object {
//        const val PREF_NAME = "encrypted_prefs"
//    }
//    // 読み込み
//    private fun encryptedLoad(KEY: String): String {
//        val mainKey = MasterKey.Builder(applicationContext)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val prefs = EncryptedSharedPreferences.create(
//            applicationContext,
//            PasswordActivity.PREF_NAME,
//            mainKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//        return prefs.getString(KEY, "")!! // nilの場合は空白を返す
//    }
//    // 保存
//    private fun encryptedSave(KEY: String, text: String) {
//        val mainKey = MasterKey.Builder(applicationContext)
//            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
//            .build()
//
//        val prefs = EncryptedSharedPreferences.create(
//            applicationContext,
//            PasswordActivity.PREF_NAME,
//            mainKey,
//            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
//            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
//        )
//        with(prefs.edit()) {
//            putString(KEY, text)
//            apply()
//        }
//    }
}