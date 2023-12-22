package com.example.ocr_digital.settings

import androidx.lifecycle.ViewModel
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsViewModel(private val activityStarterHelper: ActivityStarterHelper) : ViewModel() {

    private val auth = Firebase.auth

    fun signOut() {
        auth.signOut()
        activityStarterHelper.startActivity(MainActivity::class.java)
    }
}