package com.example.ocr_digital.startup

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class StartupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityStarterHelper = ActivityStarterHelper(this)
        val toastHelper = ToastHelper(this)
        val startupViewModel = StartupViewModel(activityStarterHelper, toastHelper)

        setContent {
            OcrdigitalTheme {
                StartupScreen(startupViewModel = startupViewModel)
            }
        }
    }
}