package com.example.ocr_digital.settings

import androidx.lifecycle.ViewModel
import com.example.ocr_digital.MainActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.onboarding.walkthrough.WalkthroughActivity
import com.example.ocr_digital.passwords.change_password.ChangePasswordActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {

    private val auth = Firebase.auth

    fun signOut() {
        auth.signOut()
        activityStarterHelper.startActivity(MainActivity::class.java)
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