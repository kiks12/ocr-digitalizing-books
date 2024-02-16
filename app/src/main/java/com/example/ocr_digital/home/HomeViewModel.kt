package com.example.ocr_digital.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.folder.FolderActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class HomeViewModel(
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        HomeState(
            folders = listOf(),
            files = listOf(),
            loading = true,
            folderName = "",
            showBottomSheet = false,
            showCreateFolderDialog = false,
            showDeleteFileOrFolderDialog = false,
            deleteForFile = false,
            showRenameFileOrFolderDialog = false,
            fileOrFolderPath = "",
            renameCurrentPath = "",
            renameNewPath = "",
            renameForFile = false,
            dialogLoading = false,
            query = ""
        )
    )

    val state : HomeState
        get() = _state.value

    init {
        refresh()
    }

    private fun showLoading() { _state.value = _state.value.copy(loading = true) }
    private fun hideLoading() { _state.value = _state.value.copy(loading = false) }

    fun refresh() {
        viewModelScope.launch {
            showLoading()
            val response = filesFolderRepository.getFilesAndFolders(auth.currentUser?.uid!!)
            val files = response.data["FILES"] as List<StorageReference>
            val folders = response.data["FOLDERS"] as List<StorageReference>
            _state.value = _state.value.copy(
                files = files,
//                backupFiles = files,
                folders = folders,
//                backupFolders = folders
            )
            hideLoading()
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

    fun showDeleteFileOrFolderDialog(path: String, forFile: Boolean = false) {
        _state.value = _state.value.copy(
            showDeleteFileOrFolderDialog = true,
            fileOrFolderPath = path,
            deleteForFile = forFile
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

    fun showDialogLoader() {
        _state.value = _state.value.copy(dialogLoading = true)
    }

    fun hideDialogLoader() {
        _state.value = _state.value.copy(dialogLoading = false)
    }

    fun openFolder(folderPath: String) {
        activityStarterHelper.startActivity(
            FolderActivity::class.java,
            stringExtras = mapOf(
                "FOLDER_PATH_EXTRA" to folderPath
            )
        )
    }

    fun onQueryChange(newString: String) {
        _state.value = _state.value.copy(query = newString)
    }

    fun searchFile() {
        viewModelScope.launch {
            showLoading()
            val result = filesFolderRepository.searchFilesFolders(query = _state.value.query, directory = auth.currentUser?.uid!!)
            _state.value = _state.value.copy(files = result.files, folders = result.folders)
            hideLoading()
        }
    }

    fun getUid() : String {
        return auth.currentUser?.uid!!
    }

}