package com.example.ocr_digital.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = Firebase.auth

        setContent {
            OcrdigitalTheme {
                Text("Home")
                Button(onClick = {
                    auth.signOut()
                    startMainActivity()
                }) {
                    Text("Sign Out")
                }
            }
        }
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}