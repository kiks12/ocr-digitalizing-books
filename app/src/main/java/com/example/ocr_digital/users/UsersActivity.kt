package com.example.ocr_digital.users

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity

class UsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val usersViewModel = UsersViewModel()

        setContent {
            UsersScreen(usersViewModel)
        }

        supportActionBar?.hide()
    }
}