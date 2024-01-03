package com.example.ocr_digital.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.folder.FolderActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class HomeViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
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
            showRenameFileOrFolderDialog = false,
            fileOrFolderPath = "",
            renameCurrentPath = "",
            renameNewPath = "",
            renameForFile = false
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
            val files = response.data["FILES"] as List<StorageReference>
            val folders = response.data["FOLDERS"] as List<StorageReference>
            _state.value = _state.value.copy(
                files = files,
                folders = folders,
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

    fun showRenameFileOrFolderDialog(currentPath: String, forFile: Boolean = false) {
        _state.value = _state.value.copy(
            showRenameFileOrFolderDialog = true,
            renameCurrentPath = currentPath,
            renameForFile = forFile
        )
    }

    fun hideRenameFileOrFolderDialog() {
        _state.value = _state.value.copy(
            showRenameFileOrFolderDialog = false,
            renameCurrentPath = "",
            renameNewPath = "",
            renameForFile = false
        )
    }

    fun onRenameNewPathChange(newString: String) {
        _state.value = _state.value.copy(renameNewPath = newString)
    }

    fun openFolder(folderPath: String) {
        activityStarterHelper.startActivity(
            FolderActivity::class.java,
            stringExtras = mapOf(
                "FOLDER_PATH_EXTRA" to folderPath
            )
        )
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
            val response = filesFolderRepository.deleteFileOrFolder(_state.value.fileOrFolderPath.substring(1))
            if (response.status == ResponseStatus.SUCCESSFUL) {
                hideDeleteFileOrFolderDialog()
                refresh()
                return@launch
            }
            toastHelper.makeToast(response.message)
        }
    }

    fun renameFileOrFolder() {
        viewModelScope.launch {
            val parentPath = PathUtilities.removeLastSegment(_state.value.renameCurrentPath)
            val newPath = if (_state.value.renameForFile) {
                parentPath + "/" + _state.value.renameNewPath + "." + PathUtilities.getFileExtension(_state.value.renameCurrentPath)
            } else {
                parentPath + "/" + _state.value.renameNewPath
            }

            if (_state.value.renameForFile) {
                val response = filesFolderRepository.renameFile(_state.value.renameCurrentPath, newPath)
                toastHelper.makeToast(response.message)
                if (response.status == ResponseStatus.SUCCESSFUL) {
                    hideRenameFileOrFolderDialog()
                    refresh()
                }
            }

        }
    }

    fun uploadImages() {
        TODO("Home View Model - Implement Upload images")
    }

    fun useCamera() {
        TODO("Home View Model - Implement Use Camera")
    }
}