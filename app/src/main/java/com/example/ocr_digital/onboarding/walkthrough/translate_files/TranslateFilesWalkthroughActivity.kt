package com.example.ocr_digital.onboarding.walkthrough.translate_files

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class TranslateFilesWalkthroughActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OcrDigitalTheme {
                TranslateFilesWalkthroughScreen {
                    finish()
                }
            }
        }

        supportActionBar?.hide()
    }
}