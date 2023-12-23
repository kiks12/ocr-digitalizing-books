package com.example.ocr_digital.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class HomeViewModel(
    private val toastHelper: ToastHelper,
) : ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        HomeState(
            folders = listOf(),
            files = listOf(),
            folderName = "",
            showBottomSheet = false,
            showCreateFolderDialog = false,
            showDeleteFileOrFolderDialog = false,
            fileOrFolderPath = ""
        )
    )

    val state : HomeState
        get() = _state.value

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val response = filesFolderRepository.getFilesAndFolders(auth.currentUser?.uid!!)
            val files = (response.data["FILES"] as List<StorageReference>)
            val folders = (response.data["FOLDERS"] as List<StorageReference>)
            _state.value = _state.value.copy(
                files = files,
                folders = folders
            )
        }
    }

    fun showBottomSheet() {
        _state.value = _state.value.copy(showBottomSheet = true)
    }

    fun hideBottomSheet() {
        _state.value = _state.value.copy(showBottomSheet = false)
    }

    fun showCreateFolderDialog() {
        _state.value = _state.value.copy(showCreateFolderDialog = true)
    }

    fun hideCreateFolderDialog() {
        _state.value = _state.value.copy(showCreateFolderDialog = false)
    }

    fun showDeleteFileOrFolderDialog(path: String) {
        _state.value = _state.value.copy(
            showDeleteFileOrFolderDialog = true,
            fileOrFolderPath = path
        )
    }

    fun hideDeleteFileOrFolderDialog() {
        _state.value = _state.value.copy(showDeleteFileOrFolderDialog = false)
    }

    fun onFolderNameChange(newString: String) {
        _state.value = _state.value.copy(folderName = newString)
    }

    fun createFolder() {
        if (_state.value.folderName.isEmpty()) {
            toastHelper.makeToast("Please input folder name")
            return
        }
        viewModelScope.launch {
            val uid = auth.currentUser?.uid!!
            val response = filesFolderRepository.createFolder("$uid/${_state.value.folderName}")
            if (response.status == ResponseStatus.SUCCESSFUL) {
                hideBottomSheet()
                hideCreateFolderDialog()
                refresh()
            } else {
                toastHelper.makeToast(response.message)
            }
        }
    }

    fun deleteFileOrFolder() {
        viewModelScope.launch {
            Log.w("FILE/FOLDER PATH", _state.value.fileOrFolderPath.substring(1))
            val response = filesFolderRepository.deleteFileOrFolder(_state.value.fileOrFolderPath.substring(1))
            if (response.status == ResponseStatus.SUCCESSFUL) {
                hideDeleteFileOrFolderDialog()
                refresh()
                return@launch
            }
            toastHelper.makeToast(response.message)
        }
    }

    fun uploadImages() {
        TODO("Home View Model - Implement Upload images")
    }

    fun useCamera() {
        TODO("Home View Model - Implement Use Camera")
    }
}