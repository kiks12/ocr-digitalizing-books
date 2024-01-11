package com.example.ocr_digital.passwords.change_password

data class ChangePasswordState(
    val currentPassword: String,
    val showCurrentPassword: Boolean,
    val newPassword: String,
    val showNewPassword: Boolean
)