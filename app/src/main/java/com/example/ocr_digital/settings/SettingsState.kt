package com.example.ocr_digital.settings

import com.example.ocr_digital.models.UserInformation

data class SettingsState(
    val authenticated : Boolean,
    val userInformation : UserInformation,
    val email : String,
)
