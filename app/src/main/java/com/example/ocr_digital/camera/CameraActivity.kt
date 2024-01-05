package com.example.ocr_digital.camera

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class CameraActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraViewModel = CameraViewModel()

        setContent {
            OcrdigitalTheme {
                CameraScreen(cameraViewModel = cameraViewModel)
            }
        }
    }
}