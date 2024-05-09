package com.example.ocr_digital.users

import com.example.ocr_digital.models.UserInformation

data class UsersState(
    val users: List<UserInformation>,
    val usersBackup: List<UserInformation>,
    val query: String,
)
