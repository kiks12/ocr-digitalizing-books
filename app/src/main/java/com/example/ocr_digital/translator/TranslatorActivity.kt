package com.example.ocr_digital.translator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.ui.res.stringResource
import com.example.ocr_digital.R
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class TranslatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val translatorViewModel = TranslatorViewModel(getString(R.string.extract_text_four), toastHelper)

        setContent {
            OcrDigitalTheme {
                TranslatorScreen(translatorViewModel) {
                    finish()
                }
            }
        }
    }
}