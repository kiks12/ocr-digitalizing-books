package com.example.ocr_digital.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.login.LoginActivity
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.onboarding.walkthrough.WalkthroughActivity
import com.example.ocr_digital.passwords.change_password.ChangePasswordActivity
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
         SettingsState(
             authenticated = false,
             userInformation = UserInformation(),
             email = "",
         )
    )
    val state : SettingsState
        get() = _state.value

    init {
        if (auth.currentUser != null) {
            viewModelScope.launch {
                val response = usersRepository.getUser(auth.currentUser?.uid!!)
                val data = response.data["user"] as List<*>
                if (response.status == ResponseStatus.SUCCESSFUL && data.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        email = auth.currentUser?.email!!,
                        userInformation = data[0] as UserInformation,
                        authenticated = true
                    )
                }
            }
        }
    }

    fun signOut() {
        auth.signOut()
        activityStarterHelper.startActivity(MainActivity::class.java)
    }
     fun startLoginActivity() {
         activityStarterHelper.startActivity(LoginActivity::class.java)
     }

    fun openChangePasswordActivity() {
        val data = auth.currentUser?.providerData
        var provider = ""
        data?.forEach { info ->
            provider += info.providerId
        }

        if (!provider.contains("password")) {
            toastHelper.makeToast("Changing Password is not applicable for provider")
            return
        }

        activityStarterHelper.startActivity(ChangePasswordActivity::class.java)
    }

    fun openWalkthroughActivity() {
        activityStarterHelper.startActivity(WalkthroughActivity::class.java)
    }
}