package com.example.ocr_digital.registration

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onStart() {
        super.onStart()

        val currentUser = auth.currentUser
        if (currentUser != null) startHomeActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val activityStarterHelper = ActivityStarterHelper(this)
        val toastHelper = ToastHelper(this)
        val registrationViewModel = RegistrationViewModel(activityStarterHelper, toastHelper)

        setContent {
            OcrdigitalTheme {
                RegistrationScreen(registrationViewModel = registrationViewModel)
            }
        }

        supportActionBar?.hide()
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}