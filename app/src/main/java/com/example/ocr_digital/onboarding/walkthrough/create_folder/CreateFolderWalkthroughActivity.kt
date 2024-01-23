package com.example.ocr_digital.onboarding.walkthrough.create_folder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class CreateFolderWalkthroughActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OcrDigitalTheme {
                CreateFolderWalkthroughScreen {
                    finish()
                }
            }
        }

        supportActionBar?.hide()
    }
}