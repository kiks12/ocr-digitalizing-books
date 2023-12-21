@file:Suppress("unused")

package com.example.ocr_digital.models

data class UserInformation(
    val profileId: String,
    val firstName: String,
    val lastName: String,
    val contactNumber: String,
    val firstTime: Boolean
) {
    constructor() : this("", "", "", "", false)
}
