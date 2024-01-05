package com.example.ocr_digital.bridge

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.example.ocr_digital.camera.CameraActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

class BridgeViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper,
    private val finishCallback: () -> Unit
): ViewModel() {
    private val _state = mutableStateOf(
        BridgeState(
            text = "",
            showSaveDialog = false,
            showResetDialog = false
        )
    )
    val state : BridgeState
        get() = _state.value

    fun onTextChange(str: String) {
        _state.value = _state.value.copy(text = str)
    }

    fun useCamera() {
        activityStarterHelper.startActivity(CameraActivity::class.java)
    }

    fun uploadImages() {
        TODO("Bridge View Model - Upload Image")
    }

    fun finish() {
        finishCallback()
    }
}