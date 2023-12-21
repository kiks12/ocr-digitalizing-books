package com.example.ocr_digital.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.lifecycle.lifecycleScope
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.repositories.UsersRepository
import com.example.ocr_digital.startup.StartupActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity : ComponentActivity() {

    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser ?: return

        lifecycleScope.launch {
            val response = usersRepository.getUser(currentUser.uid)
            if (response.status == ResponseStatus.SUCCESSFUL) {
                val user = response.data["user"] as List<*>
                if (user.isEmpty()) startStartupActivity()
            }
        }
    }

    private fun startStartupActivity() {
        val intent = Intent(this, StartupActivity::class.java)
        startActivity(intent)
        finish()
    }

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