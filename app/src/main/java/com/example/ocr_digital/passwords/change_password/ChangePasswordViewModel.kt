package com.example.ocr_digital.passwords.change_password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.repositories.UsersRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit
) : ViewModel() {
    private val usersRepository = UsersRepository()
    private val _state = mutableStateOf(
        ChangePasswordState(
            currentPassword = "",
            showCurrentPassword = false,
            newPassword = "",
            showNewPassword = false
        )
    )

    val state : ChangePasswordState
        get() = _state.value

    fun onCurrentPasswordChange(str: String) { _state.value = _state.value.copy(currentPassword = str) }
    fun onCurrentCheckboxChange(value: Boolean) { _state.value = _state.value.copy(showCurrentPassword = value) }
    fun onNewPasswordChange(str: String) { _state.value = _state.value.copy(newPassword = str) }
    fun onNewCheckboxChange(value: Boolean) { _state.value = _state.value.copy(showNewPassword = value) }

    fun changePassword() {
        viewModelScope.launch {
            val response = usersRepository.changePassword(_state.value.currentPassword, _state.value.newPassword)
            toastHelper.makeToast(response.message)
        }
    }

    fun finish() {
        finishCallback()
    }
}