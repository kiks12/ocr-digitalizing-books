package com.example.ocr_digital.folder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class FolderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val folderPath = intent.getStringExtra("FOLDER_PATH_EXTRA") ?: ""
        val toastHelper = ToastHelper(this)
        val activityStarterHelper = ActivityStarterHelper(this)
        val folderViewModel = FolderViewModel(folderPath = folderPath, activityStarterHelper = activityStarterHelper) {
            finish()
        }
        val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

        setContent {
            OcrdigitalTheme {
                FolderScreen(
                    folderViewModel = folderViewModel,
                    folderUtilityViewModel = folderUtilityViewModel
                )
            }
        }
    }
}