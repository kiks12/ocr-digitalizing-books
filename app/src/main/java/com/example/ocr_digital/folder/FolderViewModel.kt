package com.example.ocr_digital.folder

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class FolderViewModel(
    private val folderPath: String,
    private val activityStarterHelper: ActivityStarterHelper,
    private val finishCallback: () -> Unit,
): ViewModel() {

    private val filesFolderRepository = FilesFolderRepository()

    private val _state = mutableStateOf(
        FolderScreenState(
            name = PathUtilities.removeFirstSegment(folderPath),
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
        )
    )

    val state: FolderScreenState
        get() = _state.value

    init {
        refresh()
    }

    private fun showLoading() { _state.value = _state.value.copy(loading = true) }
    private fun hideLoading() { _state.value = _state.value.copy(loading = false) }

    fun refresh() {
        viewModelScope.launch {
            showLoading()
            val response = filesFolderRepository.getFilesAndFolders(folderPath)
            val files = (response.data["FILES"] as List<StorageReference>)
            val folders = (response.data["FOLDERS"] as List<StorageReference>)
            _state.value = _state.value.copy(
                files = files,
                folders = folders,
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

    fun finish() {
        finishCallback()
    }

    fun getFolderPath() : String {
        return folderPath
    }
}