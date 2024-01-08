package com.example.ocr_digital.gallery

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.camera.CameraViewModel
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class GalleryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val cameraViewModel = CameraViewModel(toastHelper = toastHelper) {
            returnData(it)
        }

        setContent {
            OcrdigitalTheme {
                GalleryScreen(cameraViewModel = cameraViewModel)
            }
        }
    }

    private fun returnData(newStr: String) {
        val intent = Intent()
        intent.putExtra("ActivityResult", newStr)
        setResult(RESULT_OK, intent)
        finish()
    }
}