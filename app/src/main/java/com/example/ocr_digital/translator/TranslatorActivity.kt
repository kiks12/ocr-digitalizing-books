package com.example.ocr_digital.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class TranslatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val string = intent.getStringExtra("FILE_CONTENT") ?: ""
        val filePath = intent.getStringExtra("FILE_PATH") ?: ""
        val folderPath = intent.getStringExtra("FOLDER_PATH") ?: ""
        val toastHelper = ToastHelper(this)
        val translatorViewModel = TranslatorViewModel(string, filePath, folderPath, toastHelper) {
            finish()
        }

        setContent {
            OcrDigitalTheme {
                TranslatorScreen(translatorViewModel)
            }
        }

        supportActionBar?.hide()
    }
}