package com.tokudai0000.tokumemo.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.tokudai0000.tokumemo.R
import com.tokudai0000.tokumemo.ui.agreement.AgreementActivity
import com.tokudai0000.tokumemo.ui.splash.SplashActivity

class RootActivity : AppCompatActivity(R.layout.activity_root) {
    companion object {
        const val EXTRA_NEXT_ACTIVITY = "extra_next_activity"
    }

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val nextActivity = result.data?.getStringExtra(EXTRA_NEXT_ACTIVITY)
            when (nextActivity) {
                "SplashActivity" -> switchToSplash()
                "AgreementActivity" -> switchToAgreement()
                "MainActivity" -> switchToMain()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        switchToSplash()
    }

    override fun onResume() {
        super.onResume()

        val nextActivity = intent.getStringExtra(EXTRA_NEXT_ACTIVITY)
        when (nextActivity) {
            "SplashActivity" -> switchToSplash()
            "AgreementActivity" -> switchToAgreement()
            "MainActivity" -> switchToMain()
        }
    }

    private fun switchToSplash() {
        val intent = SplashActivity.createIntent(this)
        resultLauncher.launch(intent)
    }

    private fun switchToAgreement() {
        val intent = AgreementActivity.createIntent(this)
        resultLauncher.launch(intent)
    }

//    private fun switchToWeb() {
//        val intent = AgreementActivity.createIntent(this)
//        resultLauncher.launch(intent)
//    }

    private fun switchToMain() {
        val intent = MainActivity.createIntent(this)
        resultLauncher.launch(intent)
    }
}