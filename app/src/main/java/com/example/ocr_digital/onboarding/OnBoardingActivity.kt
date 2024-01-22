package com.example.ocr_digital.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.onboarding.screens.OnBoardingScreen
import com.example.ocr_digital.repositories.UsersRepository
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OnBoardingActivity : AppCompatActivity() {

    private val auth = Firebase.auth
    private val usersRepository = UsersRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OcrdigitalTheme {
                OnBoardingScreen(
                    startHomeActivity = { lifecycleScope.launch { startHomeActivity() } }
                )
            }
        }

        supportActionBar?.hide()
    }

    private suspend fun startHomeActivity() {
        lifecycleScope.launch {
            val authUid = auth.currentUser?.uid ?: return@launch
            val uid = usersRepository.getUid(authUid) ?: return@launch
            usersRepository.onboardUser(uid)
        }
        delay(3000)
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}