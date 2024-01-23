package com.example.ocr_digital.onboarding.walkthrough

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.home.HomeActivity
import com.example.ocr_digital.onboarding.walkthrough.create_folder.CreateFolderWalkthroughActivity
import com.example.ocr_digital.onboarding.walkthrough.extract_text.ExtractTextWalkthroughActivity
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WalkthroughViewModel(
    automatic: Boolean,
    private val activityStarterHelper: ActivityStarterHelper,
    private val finishCallback: () -> Unit
): ViewModel() {
    private val auth = Firebase.auth
    private val usersRepository = UsersRepository()
    private val _loadingState = mutableStateOf(false)
    private val _automaticState = mutableStateOf(automatic)
    val automaticState : Boolean
        get() = _automaticState.value

    val loadingState : Boolean
        get() = _loadingState.value

    fun startCreateFolderWalkthroughActivity() {
        activityStarterHelper.startActivity(CreateFolderWalkthroughActivity::class.java)
    }

    fun startExtractTextWalkthroughActivity() {
        activityStarterHelper.startActivity(ExtractTextWalkthroughActivity::class.java)
    }

    fun finish() {
        finishCallback()
    }

    fun finishAutomatic() {
        viewModelScope.launch {
            _loadingState.value = true
            val authUid = auth.currentUser?.uid ?: return@launch
            val uid = usersRepository.getUid(authUid) ?: return@launch
            usersRepository.finishUserWalkthrough(uid)
            delay(3000)
            activityStarterHelper.startActivity(HomeActivity::class.java)
        }
    }
}