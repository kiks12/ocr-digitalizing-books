package com.example.ocr_digital.image_scanner

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.file_saver.FileMetadata
import com.example.ocr_digital.file_saver.FileSaver
import com.example.ocr_digital.file_saver.FileType
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.Timestamp
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ImageScannerViewModel(
    private val path: String,
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit
): ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val fileSaver = FileSaver()
    private val _state = mutableStateOf(
        ImageScannerState(
            text = "",
            showSaveDialog = false,
            filename = "",
            filetype = FileType.DOCX,
            showResetDialog = false,
            loading = false,
            selectedFolder = path,
            parentFolder = path,
            folders = listOf(),
            showCreateFolderDialog = false,
            folderName = "",
            bookTitle = "",
            author = "",
            publishYear = "",
            genre = ""
        )
    )

    init {
        getFolders()
    }

    val state : ImageScannerState
        get() = _state.value

    fun getFolders() {
        viewModelScope.launch {
            val response = filesFolderRepository.getFolders(path)
            _state.value = _state.value.copy(folders = response.data["FOLDERS"] as List<StorageReference>)
        }
    }

    fun onTextChange(str: String) {
        _state.value = _state.value.copy(text = str)
    }

    fun hideSaveDialog() { _state.value = _state.value.copy(showSaveDialog = false) }

    fun showSaveDialog() { _state.value = _state.value.copy(showSaveDialog = true) }

    fun hideResetDialog() { _state.value = _state.value.copy(showResetDialog = false) }

    fun showResetDialog() { _state.value = _state.value.copy(showResetDialog = true) }

    fun showCreateFolderDialog() { _state.value = _state.value.copy(showCreateFolderDialog = true) }
    fun hideCreateFolderDialog() { _state.value = _state.value.copy(showCreateFolderDialog = false) }

    private fun showLoading() { _state.value = _state.value.copy(loading = true) }

    private fun hideLoading() { _state.value = _state.value.copy(loading = false) }

    fun resetText() {
        _state.value = _state.value.copy(text = "", showResetDialog = false)
    }

    fun onFolderNameChange(newStr: String) { _state.value = _state.value.copy(folderName = newStr) }

    fun onFileTypeChange(type: FileType) { _state.value = _state.value.copy(filetype = type) }

    fun onFileNameChange(newStr: String) { _state.value = _state.value.copy(filename = newStr) }
    fun onBookTitleChange(newStr: String) { _state.value = _state.value.copy(bookTitle = newStr) }
    fun onAuthorChange(newStr: String) { _state.value = _state.value.copy(author = newStr) }
    fun onPublishYearChange(newStr: String) { _state.value = _state.value.copy(publishYear = newStr) }
    fun onGenreChange(newStr: String) { _state.value = _state.value.copy(genre = newStr) }

    fun saveFile(context: Context, text: String, filename: String, type: FileType) {
        if (filename == "") {
            toastHelper.makeToast("Empty filename! Please enter a filename")
            return
        }

        val uri = fileSaver.saveTextToFile(context, text, filename, type)
        val uriText = fileSaver.saveTextToFile(context, text, filename, FileType.TXT)
        if (uri != null && uriText != null) {
            viewModelScope.launch {
                var responseMessage : String? = null
                async {
                    hideResetDialog()
                    hideSaveDialog()
                    showLoading()
                    val response = filesFolderRepository.uploadFile(_state.value.selectedFolder, uri)
                    val responseTwo = filesFolderRepository.uploadFile(_state.value.selectedFolder, uriText)
                    val path = "${_state.value.selectedFolder}/${uri.lastPathSegment}".replace("/", "___")
                    val responseThree = filesFolderRepository.uploadFileMetadata(
                        FileMetadata(
                            title = _state.value.bookTitle,
                            author = _state.value.author,
                            genre = _state.value.genre,
                            publishedYear = _state.value.publishYear,
                            createdAt = Timestamp.now(),
                            path = path
                        )
                    )
                    if (response.status == ResponseStatus.SUCCESSFUL && responseTwo.status == ResponseStatus.SUCCESSFUL && responseThree.status == ResponseStatus.SUCCESSFUL) {
                        responseMessage = response.message
                    }
                    if (response.status == ResponseStatus.FAILED) responseMessage = response.message
                    if (responseTwo.status == ResponseStatus.FAILED) responseMessage = responseTwo.message
                    if (responseThree.status == ResponseStatus.FAILED) responseMessage = responseThree.message
                    hideSaveDialog()
                }.await()
                async {
                    hideLoading()
                    if (responseMessage != null) toastHelper.makeToast(responseMessage!!)
                }.await()
            }
        }
    }

    fun setSelectedFolder(path: String) {
        viewModelScope.launch {
            val response = filesFolderRepository.getFolders(path)
            _state.value = _state.value.copy(selectedFolder = path, folders = response.data["FOLDERS"] as List<StorageReference>)
        }
    }

    fun finish() {
        finishCallback()
    }
}