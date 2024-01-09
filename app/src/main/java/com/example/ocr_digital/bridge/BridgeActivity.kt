package com.example.ocr_digital.bridge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class BridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val path = intent.getStringExtra("PATH") ?: ""

        val toastHelper = ToastHelper(this)
        val bridgeViewModel = BridgeViewModel(path = path, toastHelper = toastHelper) {
            finish()
        }

        setContent {
            OcrdigitalTheme {
                BridgeScreen(bridgeViewModel = bridgeViewModel)
            }
        }
    }

}