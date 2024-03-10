package com.example.ocr_digital.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.onboarding.walkthrough.WalkthroughActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeViewModel(
    private val activityStarterHelper: ActivityStarterHelper,
): ViewModel() {
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        HomeState(
            displayName = ""
        )
    )
    val state: HomeState
        get() = _state.value

    init {
        _state.value = _state.value.copy(displayName = auth.currentUser?.displayName!!)
    }

    fun startWalkthroughActivity() {
        activityStarterHelper.startActivity(WalkthroughActivity::class.java)
    }
}