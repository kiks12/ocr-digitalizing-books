package com.example.ocr_digital.bridge

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class BridgeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val activityStarterHelper = ActivityStarterHelper(this)
        val bridgeViewModel = BridgeViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper) {
            finish()
        }

        setContent {
            OcrdigitalTheme {
                BridgeScreen(bridgeViewModel = bridgeViewModel)
            }
        }
    }
}