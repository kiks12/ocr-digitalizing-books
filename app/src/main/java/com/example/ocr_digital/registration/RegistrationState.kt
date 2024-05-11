package com.example.ocr_digital.registration

data class RegistrationState(
    val email: String,
    val password: String,
    val seePassword: Boolean = false,
    val firstName: String,
    val lastName: String,
    val contactNumber: String,
    val admin: Boolean,
)