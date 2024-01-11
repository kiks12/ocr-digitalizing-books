package com.example.ocr_digital.passwords.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.ui.theme.OcrdigitalTheme

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val forgotPasswordViewModel = ForgotPasswordViewModel(toastHelper = toastHelper) {
            finish()
        }

        setContent {
            OcrdigitalTheme {
                ForgotPasswordScreen(forgotPasswordViewModel = forgotPasswordViewModel)
            }
        }

        supportActionBar?.hide()
    }
}