package com.example.ocr_digital.passwords.forgot_password

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.repositories.UsersRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit
): ViewModel() {
    private val usersRepository = UsersRepository()
    private val _state = mutableStateOf("")

    val state : String
        get() = _state.value

    fun onEmailChange(str: String) { _state.value = str }

    fun sendResetEmail() {
        viewModelScope.launch {
            val response = usersRepository.resetPassword(_state.value)
            toastHelper.makeToast(response.message)
        }
    }

    fun finish() {
        finishCallback()
    }
}