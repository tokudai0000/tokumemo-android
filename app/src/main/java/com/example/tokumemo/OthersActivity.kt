package com.example.tokumemo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.tokumemo.manager.DataManager

class OthersActivity : AppCompatActivity() {

    override fun onBackPressed() {
        // Android戻るボタン無効
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_others)

        val settingsScreen = findViewById<LinearLayout>(R.id.settings_screen)
        val title = findViewById<TextView>(R.id.settings_title)
        val bar = findViewById<ConstraintLayout>(R.id.backBar)
        val back = findViewById<Button>(R.id.back)
        val aboutThisAppText = findViewById<ScrollView>(R.id.aboutThisAppText)

        // メニューバー
        val home = findViewById<Button>(R.id.home)
        home.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val news = findViewById<Button>(R.id.news)
        news.setOnClickListener{
            val intent = Intent(this, NewsActivity::class.java)
            startActivity(intent)
            finish()
        }

        val clubList = findViewById<Button>(R.id.review)
        clubList.setOnClickListener{
            val intent = Intent(this, ClubListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val others = findViewById<Button>(R.id.others)
        others.setOnClickListener{
            val intent = Intent(this, OthersActivity::class.java)
            startActivity(intent)
            finish()
        }

        // パスワード設定を押したとき
        val passwordSetting = findViewById<Button>(R.id.passwordSetting)
        passwordSetting.setOnClickListener{
            val intent = Intent(this, PasswordActivity::class.java)
            startActivity(intent)
        }

        // お問い合わせを押したとき
        val inquiry = findViewById<Button>(R.id.inquiry)
        inquiry.setOnClickListener{
            goWeb("https://docs.google.com/forms/d/e/1FAIpQLScYRhlWY9IjqWOrvnWJ0bJ_yPQZpXy4PPShWb68092t2klzeg/viewform")
        }

        // 公式SNSを押したとき
        val sns = findViewById<Button>(R.id.sns)
        sns.setOnClickListener{
            goWeb("https://twitter.com/tokumemo0000")
        }

        // ホームページを押したとき
        val homePage = findViewById<Button>(R.id.homePage)
        homePage.setOnClickListener{
            goWeb("https://lit.link/developers")
        }

        // 利用規約を押したとき
        val termsOfService = findViewById<Button>(R.id.termsOfService)
        termsOfService.setOnClickListener{
            goWeb("https://github.com/tokudai0000/document/blob/main/tokumemo/terms/TermsOfService.txt")
        }

        // プライバシーポリシーを押したとき
        val privacyPolicy = findViewById<Button>(R.id.privacyPolicy)
        privacyPolicy.setOnClickListener{
            goWeb("https://github.com/tokudai0000/document/blob/main/tokumemo/terms/PrivacyPolicy.txt")
        }

        // ソースコードを押したとき
        val sourceCode = findViewById<Button>(R.id.sourceCode)
        sourceCode.setOnClickListener{
            goWeb("https://github.com/tokudai0000/TokumemoAndroid/tree/master")
        }

        // このアプリについてを押したとき
        val aboutThisApp = findViewById<Button>(R.id.aboutThisApp)
        aboutThisApp.setOnClickListener{
            bar.visibility = View.VISIBLE
            aboutThisAppText.visibility = View.VISIBLE
        }

        // 戻るボタンを押したとき
        back.setOnClickListener{
            settingsScreen.visibility = View.VISIBLE
            title.visibility = View.VISIBLE

            aboutThisAppText.visibility = View.INVISIBLE
            bar.visibility = View.INVISIBLE
        }
    }

    // 押されたWebサイトにとぶ
    private fun goWeb(pageId: String) {
        val intent = Intent(this, WebActivity::class.java)
        // WebActivityにどのWebサイトを開こうとしているかをIdとして送信して知らせる
        intent.putExtra("PAGE_KEY",pageId)
        // 自動入力のフラグを上げる
        DataManager.canExecuteJavascript = true
        startActivity(intent)
    }
}