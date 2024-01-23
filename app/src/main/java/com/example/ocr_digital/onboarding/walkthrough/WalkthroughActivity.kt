package com.example.ocr_digital.onboarding.walkthrough

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class WalkthroughActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val automatic = intent.getBooleanExtra("AUTOMATIC", false)
        val activityStarterHelper = ActivityStarterHelper(this)
        val walkthroughViewModel = WalkthroughViewModel(automatic, activityStarterHelper) {
            finish()
        }

        setContent {
            OcrDigitalTheme {
                WalkthroughScreen(walkthroughViewModel)
            }
        }

        supportActionBar?.hide()
    }
}