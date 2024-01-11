package com.example.ocr_digital.passwords.change_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val changePasswordViewModel = ChangePasswordViewModel(toastHelper) {
            finish()
        }

        setContent {
            OcrdigitalTheme {
                ChangePasswordScreen(changePasswordViewModel)
            }
        }

        supportActionBar?.hide()
    }
}