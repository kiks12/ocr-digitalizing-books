package com.example.ocr_digital.bridge

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.file_saver.FileSaver
import com.example.ocr_digital.file_saver.FileType
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.repositories.FilesFolderRepository
import kotlinx.coroutines.launch

class BridgeViewModel(
    private val path: String,
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit
): ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val fileSaver = FileSaver()
    private val _state = mutableStateOf(
        BridgeState(
            text = "",
            showSaveDialog = false,
            filename = "",
            filetype = FileType.DOCX,
            showResetDialog = false
        )
    )
    val state : BridgeState
        get() = _state.value

    fun onTextChange(str: String) {
        _state.value = _state.value.copy(text = str)
    }

    fun hideSaveDialog() { _state.value = _state.value.copy(showSaveDialog = false) }

    fun showSaveDialog() { _state.value = _state.value.copy(showSaveDialog = true) }

    fun hideResetDialog() { _state.value = _state.value.copy(showResetDialog = false) }

    fun showResetDialog() { _state.value = _state.value.copy(showResetDialog = true) }

    fun resetText() {
        _state.value = _state.value.copy(text = "", showResetDialog = false)
    }

    fun onFileTypeChange(type: FileType) { _state.value = _state.value.copy(filetype = type) }

    fun onFileNameChange(newStr: String) { _state.value = _state.value.copy(filename = newStr) }

    fun saveFile(context: Context, text: String, filename: String, type: FileType) {
        val uri = fileSaver.saveTextToFile(context, text, filename, type)
        if (uri != null) {
            viewModelScope.launch {
                val response = filesFolderRepository.uploadFile(path, uri)
                if (response.status == ResponseStatus.SUCCESSFUL) {
                    hideSaveDialog()
                }

                toastHelper.makeToast(response.message)
            }
        }
    }

    fun finish() {
        finishCallback()
    }
}