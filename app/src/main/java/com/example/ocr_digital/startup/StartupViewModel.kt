package com.example.ocr_digital.startup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class StartupViewModel(
    private val activityStarterHelper: ActivityStarterHelper,
    private val toastHelper: ToastHelper
) : ViewModel() {
    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        StartupState(
            firstName = "",
            lastName = "",
            contactNumber = ""
        )
    )

    val state : StartupState
        get() = _state.value

    fun onFirstNameChange(newString: String) {
        _state.value = _state.value.copy(firstName = newString)
    }

    fun onLastNameChange(newString: String) {
        _state.value = _state.value.copy(lastName= newString)
    }

    fun onContactNumberChange(newString: String) {
        _state.value = _state.value.copy(contactNumber = newString)
    }

    private fun isValid() : Boolean {
        if (_state.value.firstName.isEmpty()) return false
        if (_state.value.lastName.isEmpty()) return false
        if (_state.value.contactNumber.isEmpty()) return false
        return true
    }

    fun register() {
        if (!isValid()) {
            toastHelper.makeToast("Please make sure to fill up all the fields")
            return
        }

        val currentUser = auth.currentUser ?: return
        viewModelScope.launch {
            val response = usersRepository.saveUserInformation(
                newUser = UserInformation(
                    profileId = currentUser.uid,
                    firstName = _state.value.firstName,
                    lastName = _state.value.lastName,
                    contactNumber = _state.value.contactNumber,
                    firstTime = true
                )
            )
            toastHelper.makeToast(response.message)
            if (response.status == ResponseStatus.SUCCESSFUL) startHomeActivity()
        }
    }

    fun signOut() {
        auth.signOut()
        startMainActivity()
    }

    private fun startMainActivity() {
        activityStarterHelper.startActivity(MainActivity::class.java)
    }

    private fun startHomeActivity() {
        activityStarterHelper.startActivity(HomeActivity::class.java)
    }
}