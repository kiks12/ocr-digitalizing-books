package com.example.ocr_digital.image_scanner

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class ImageScannerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val path = intent.getStringExtra("PATH") ?: ""

        val toastHelper = ToastHelper(this)
        val imageScannerViewModel = ImageScannerViewModel(path = path, toastHelper = toastHelper) {
            finish()
        }

        setContent {
            OcrDigitalTheme {
                ImageScannerScreen(imageScannerViewModel = imageScannerViewModel)
            }
        }

        supportActionBar?.hide()
    }

}