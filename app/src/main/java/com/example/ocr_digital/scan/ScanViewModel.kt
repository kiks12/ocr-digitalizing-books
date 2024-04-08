package com.example.ocr_digital.scan

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.folder.FolderActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.example.ocr_digital.repositories.UsersRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class ScanViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val usersRepository = UsersRepository()
    private val filesFolderRepository = FilesFolderRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        ScanState(
            folders = listOf(),
            files = listOf(),
            loading = true,
            folderName = "",
            filePath = "",
            showCopyToDialog = false,
            showCreateFolderDialog = false,
            showDeleteFileOrFolderDialog = false,
            deleteForFile = false,
            showRenameFileOrFolderDialog = false,
            fileOrFolderPath = "",
            renameCurrentPath = "",
            renameNewPath = "",
            renameForFile = false,
            dialogLoading = false,
            query = "",
            publicAdminUID = "",
            parentFolder = "",
            selectedFolder = "",
            copyToFolders = listOf()
        )
    )

    val state : ScanState
        get() = _state.value

    init {
        viewModelScope.launch {
            if (auth.currentUser != null) {
                _state.value = _state.value.copy(parentFolder = getPath(), selectedFolder = getPath())
                getFolders()
            }
            getPublicAdmin()
            refresh()
        }
    }

    private fun showLoading() { _state.value = _state.value.copy(loading = true) }
    private fun hideLoading() { _state.value = _state.value.copy(loading = false) }

    private fun getFolders() {
        viewModelScope.launch {
            val response = filesFolderRepository.getFolders(getPath())
            _state.value = _state.value.copy(copyToFolders = response.data["FOLDERS"] as List<StorageReference>)
        }
    }

    fun refresh() {
        if (auth.currentUser != null) getUserFiles()
        else getPublicLibrary()
    }

    private fun getPublicAdmin() {
        viewModelScope.launch {
            val publicAdminResponse = usersRepository.getPublicAdminUser()
            val publicAdminAccount = (publicAdminResponse.data["user"] as List<UserInformation>)[0]
            val publicAdminUID = usersRepository.getUid(publicAdminAccount.profileId)
            _state.value = _state.value.copy(publicAdminUID = publicAdminUID!!)
        }
    }

    private fun getPublicLibrary() {
        viewModelScope.launch {
            showLoading()
            val publicAdminResponse = usersRepository.getPublicAdminUser()
            val publicAdminAccount = (publicAdminResponse.data["user"] as List<UserInformation>)[0]
            val publicAdminUID = usersRepository.getUid(publicAdminAccount.profileId)
            val response = filesFolderRepository.getFilesAndFolders(publicAdminUID!!)
            val files = response.data["FILES"] as List<StorageReference>
            val folders = response.data["FOLDERS"] as List<StorageReference>
            _state.value = _state.value.copy(
                files = files,
                folders = folders,
            )
            hideLoading()
        }
    }

    private fun getUserFiles() {
        viewModelScope.launch {
            showLoading()
            val response = filesFolderRepository.getFilesAndFolders(auth.currentUser?.uid!!)
            val files = response.data["FILES"] as List<StorageReference>
            val folders = response.data["FOLDERS"] as List<StorageReference>
            _state.value = _state.value.copy(
                files = files,
                folders = folders,
            )
            hideLoading()
        }
    }

    fun showCreateFolderDialog() {
        if (auth.currentUser != null) {
            _state.value = _state.value.copy(showCreateFolderDialog = true)
        } else {
            showUnauthenticatedLoginMessage()
        }
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

    fun showCopyToDialog(filePath: String) {
        _state.value = _state.value.copy(showCopyToDialog = true, filePath = filePath)
    }

    fun hideCopyToDialog() {
        _state.value = _state.value.copy(showCopyToDialog = false)
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

    fun getPublicAdminUID() : String {
        return _state.value.publicAdminUID
    }

    fun isAuthenticated() : Boolean {
        return auth.currentUser != null
    }

    fun showUnauthenticatedLoginMessage() {
        toastHelper.makeToast("Please login to enable feature")
    }

    private fun getPath() : String {
        return "/${getUid()}"
    }

    fun setSelectedFolder(path: String) {
        viewModelScope.launch {
            if (path == "Public Library") {
                val response = filesFolderRepository.getFolders("/${getPublicAdminUID()}")
                _state.value = _state.value.copy(selectedFolder = path, copyToFolders = response.data["FOLDERS"] as List<StorageReference>)
            } else {
                val response = filesFolderRepository.getFolders(path)
                _state.value = _state.value.copy(selectedFolder = path, copyToFolders = response.data["FOLDERS"] as List<StorageReference>)
            }
        }
    }

    fun copyFileToSelectedFolder() {
        viewModelScope.launch {
            val response = filesFolderRepository.copyFile(_state.value.filePath, _state.value.selectedFolder + "/" + PathUtilities.getLastSegment(_state.value.filePath))
            toastHelper.makeToast(response.message)
        }
    }

}