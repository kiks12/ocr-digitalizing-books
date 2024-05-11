package com.example.ocr_digital.users

data class UserResult(
    val uid: String,
    val email: String,
    val emailVerified: Boolean,
    val disabled: Boolean,
    val admin: Boolean,
    val firstName: String,
    val lastName: String
)
