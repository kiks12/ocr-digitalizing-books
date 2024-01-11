package com.example.ocr_digital.camera

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val cameraViewModel = CameraViewModel(
            toastHelper = toastHelper,
            returnData = { returnData(it) },
            finishCallback = { finish() }
        )

        setContent {
            OcrdigitalTheme {
                CameraScreen(cameraViewModel = cameraViewModel)
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