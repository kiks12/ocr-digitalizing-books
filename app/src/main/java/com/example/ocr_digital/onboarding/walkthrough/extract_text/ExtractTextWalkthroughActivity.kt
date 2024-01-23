package com.example.ocr_digital.onboarding.walkthrough.extract_text

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class ExtractTextWalkthroughActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OcrDigitalTheme {
                ExtractTextWalkthroughScreen {
                    finish()
                }
            }
        }

        supportActionBar?.hide()
    }
}