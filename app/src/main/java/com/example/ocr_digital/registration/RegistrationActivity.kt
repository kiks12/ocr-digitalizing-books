package com.example.ocr_digital.registration

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ocr_digital.api.UsersAPI
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.RetrofitHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.ui.theme.OcrDigitalTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected == true
    }

    override fun onStart() {
        super.onStart()
        val forAdmin = intent.getStringExtra("forAdmin")

        val currentUser = auth.currentUser
        if (currentUser != null && forAdmin == null) startHomeActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val forAdmin = intent.getStringExtra("forAdmin")

        auth = Firebase.auth

        val activityStarterHelper = ActivityStarterHelper(this)
        val toastHelper = ToastHelper(this)
        val usersApi = RetrofitHelper.getInstance(::isNetworkAvailable)?.create(UsersAPI::class.java)
        val registrationViewModel = RegistrationViewModel(
            forAdmin != null,
            usersApi!!,
            activityStarterHelper,
            toastHelper
        ) { finish() }

        setContent {
            OcrDigitalTheme {
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