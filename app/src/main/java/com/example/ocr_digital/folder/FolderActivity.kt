package com.example.ocr_digital.folder

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

class FolderActivity : AppCompatActivity() {

    private lateinit var folderViewModel: FolderViewModel

    override fun onResume() {
        super.onResume()
        folderViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val folderPath = intent.getStringExtra("FOLDER_PATH_EXTRA") ?: ""
        val toastHelper = ToastHelper(this)
        val activityStarterHelper = ActivityStarterHelper(this)
        folderViewModel = FolderViewModel(folderPath = folderPath, toastHelper = toastHelper, activityStarterHelper = activityStarterHelper) {
            finish()
        }
        val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

        setContent {
            OcrDigitalTheme {
                FolderScreen(
                    folderViewModel = folderViewModel,
                    folderUtilityViewModel = folderUtilityViewModel
                )
            }
        }

        supportActionBar?.hide()
    }
}