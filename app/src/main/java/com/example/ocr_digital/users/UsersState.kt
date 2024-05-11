package com.example.ocr_digital.users

data class UsersState(
    val loading: Boolean,
    val users: List<UserResult>,
    val usersBackup: List<UserResult>,
    val query: String,
    val admin: Boolean,
    val firstName: String,
    val lastName: String,
    val uid: String,
    val contactNumber: String,
    val showEditUserDialog: Boolean,
    val editUserDialogLoading: Boolean,
    val showEnableConfirmationDialog: Boolean,
    val enableConfirmationLoading: Boolean,
    val showDisableConfirmationDialog: Boolean,
    val disableConfirmationLoading: Boolean,
    val showDeleteConfirmationDialog: Boolean,
    val deleteConfirmationLoading: Boolean,
)
