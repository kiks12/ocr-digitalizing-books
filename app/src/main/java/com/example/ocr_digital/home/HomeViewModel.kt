package com.example.ocr_digital.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.onboarding.walkthrough.WalkthroughActivity
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val activityStarterHelper: ActivityStarterHelper,
): ViewModel() {
    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        HomeState(
            displayName = ""
        )
    )
    val state: HomeState
        get() = _state.value

    init {
        viewModelScope.launch {
            val response = usersRepository.getUser(auth.currentUser?.uid!!)
            val user = (response.data["user"] as List<UserInformation>)[0]
            _state.value = _state.value.copy(displayName = "${user.firstName} ${user.lastName}")
        }
    }

    fun startWalkthroughActivity() {
        activityStarterHelper.startActivity(WalkthroughActivity::class.java)
    }
}