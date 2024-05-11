@file:Suppress("unused")

package com.example.ocr_digital.models

data class UserInformation(
    val profileId: String,
    val firstName: String,
    val lastName: String,
    val contactNumber: String,
    val onboarding: Boolean,
    val walkthrough: Boolean,
    val admin: Boolean = false
) {
    constructor() : this("", "", "", "", false, false, false)
}
