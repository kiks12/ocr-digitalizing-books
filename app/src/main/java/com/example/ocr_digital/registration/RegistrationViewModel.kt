package com.example.ocr_digital.registration

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.api.UsersAPI
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.login.LoginActivity
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class RegistrationViewModel(
    val forAdmin: Boolean = false,
    private val usersAPI : UsersAPI,
    private val activityStarterHelper: ActivityStarterHelper,
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit,
): ViewModel() {
    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth

    private val _state = mutableStateOf(
        RegistrationState(
            email = "",
            password = "",
            firstName = "",
            lastName = "",
            contactNumber = "",
            admin = false,
        )
    )

    val state : RegistrationState
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

    fun onFirstNameChange(newString: String) {
        _state.value = _state.value.copy(
            firstName = newString
        )
    }

    fun onLastNameChange(newString: String) {
        _state.value = _state.value.copy(
            lastName = newString
        )
    }

    fun onContactNumberChange(newString: String) {
        _state.value = _state.value.copy(
            contactNumber = newString
        )
    }

    fun onAdminChange(newVal: Boolean) { _state.value = _state.value.copy(admin = newVal) }

    fun register() {
        if (isValid()) {
            if (forAdmin) {
                viewModelScope.launch {
                    val result = usersAPI.createUser(mapOf(
                        "email" to _state.value.email,
                        "password" to _state.value.password,
                        "contactNumber" to _state.value.password,
                        "firstName" to _state.value.firstName,
                        "lastName" to _state.value.lastName,
                        "admin" to _state.value.admin.toString()
                    ))
                    toastHelper.makeToast(result.body()?.message!!)
                    finishCallback()
                }
            } else {
                auth.createUserWithEmailAndPassword(_state.value.email, _state.value.password)
                    .addOnSuccessListener { result ->
                        toastHelper.makeToast("Registered new account successfully")
                        viewModelScope.launch {
                            val response = usersRepository.saveUserInformation(
                                UserInformation(
                                    profileId = result.user?.uid!!,
                                    firstName = _state.value.firstName,
                                    lastName = _state.value.lastName,
                                    contactNumber = _state.value.contactNumber,
                                    admin = _state.value.admin,
                                    onboarding = true,
                                    walkthrough = true
                                )
                            )
                            toastHelper.makeToast(response.message)
                            startMainActivity()
                        }
                    }
                    .addOnFailureListener {
                        toastHelper.makeToast("Account registration unsuccessful, try again")
                    }
            }

            return
        }
        toastHelper.makeToast("Please make sure to fill up all the fields")
    }

    private fun isValid() : Boolean {
        if (_state.value.email.isEmpty()) return false
        if (_state.value.password.isEmpty()) return false
        if (_state.value.firstName.isEmpty()) return false
        if (_state.value.lastName.isEmpty()) return false
        if (_state.value.contactNumber.isEmpty()) return false
        return true
    }

    private fun startMainActivity() {
        activityStarterHelper.startActivity(MainActivity::class.java)
    }

    fun startLoginActivity() {
        activityStarterHelper.startActivity(LoginActivity::class.java)
    }

    fun finishActivity() {
        finishCallback()
    }
}