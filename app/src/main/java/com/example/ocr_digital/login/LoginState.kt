package com.example.ocr_digital.login

data class LoginState(
    val email: String,
    val password: String,
    val seePassword: Boolean = false,
)