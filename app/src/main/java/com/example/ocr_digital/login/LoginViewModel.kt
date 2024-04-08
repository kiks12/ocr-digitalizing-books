package com.example.ocr_digital.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.passwords.forgot_password.ForgotPasswordActivity
import com.example.ocr_digital.registration.RegistrationActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginViewModel(
    private val activityStarterHelper: ActivityStarterHelper,
    private val toastHelper: ToastHelper,
    private val continueWithGoogle: () -> Unit
) : ViewModel() {
    private val auth = Firebase.auth

    private val _state = mutableStateOf(
        LoginState(
            email = "",
            password = ""
        )
    )

    val state : LoginState
        get() = _state.value

    fun onEmailChange(newString: String) {
        _state.value = _state.value.copy(
            email = newString
        )
    }

    fun onPasswordChange(newString: String) {
        _state.value = _state.value.copy(
            password = newString
        )
    }

    fun onSeePasswordChange(newValue: Boolean) {
        _state.value = _state.value.copy(
            seePassword = newValue
        )
    }

    fun loginWithEmailAndPassword() {
        if (credentialsAreValid()) {
            auth.signInWithEmailAndPassword(_state.value.email, _state.value.password)
                .addOnSuccessListener { result ->
                    toastHelper.makeToast("Signed in as ${result.user?.email}")
                    startHomeActivity()
                }
                .addOnFailureListener {
                    toastHelper.makeToast(it.message.toString())
                }
            return
        }
        toastHelper.makeToast("Please fill up all the field to continue")
    }

    private fun credentialsAreValid() : Boolean {
        if (_state.value.email.isEmpty()) return false
        if (_state.value.password.isEmpty()) return false
        return true
    }

    fun continueWithGoogleHelper() {
        continueWithGoogle()
    }

    fun startHomeActivity() {
        activityStarterHelper.startActivity(HomeActivity::class.java)
    }

    fun startRegistrationActivity() {
        activityStarterHelper.startActivity(RegistrationActivity::class.java)
    }

    fun startForgotPasswordActivity() {
        activityStarterHelper.startActivity(ForgotPasswordActivity::class.java)
    }
}