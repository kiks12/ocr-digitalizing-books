package com.example.ocr_digital.folder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import kotlinx.coroutines.launch

class FolderUtilityViewModel(
    private val toastHelper: ToastHelper
) : ViewModel() {

    private val filesFolderRepository = FilesFolderRepository()

    fun createFolder(folderName: String, folderPath: String, successCallback: (str: String) -> Unit, failedCallback: (str: String) -> Unit = {}) {
        if (folderName.isEmpty()) {
            toastHelper.makeToast("Please input folder name")
            return
        }
        viewModelScope.launch {
            val response = filesFolderRepository.createFolder("$folderPath/$folderName")
            if (response.status == ResponseStatus.SUCCESSFUL) {
                toastHelper.makeToast(response.message)
                successCallback(response.message)
                return@launch
            }

            toastHelper.makeToast(response.message)
            failedCallback(response.message)
        }
    }

    fun deleteFileOrFolder(fileFolderPath: String, successCallback: (newStr: String) -> Unit, failedCallback: (str: String) -> Unit = {}) {
        viewModelScope.launch {
            val response = filesFolderRepository.deleteFileOrFolder(fileFolderPath.substring(1))
            if (response.status == ResponseStatus.SUCCESSFUL) {
                toastHelper.makeToast(response.message)
                successCallback(response.message)
                return@launch
            }

            toastHelper.makeToast(response.message)
            failedCallback(response.message)
        }
    }

    fun renameFileOrFolder(renameCurrentPath: String, renameNewPath: String, renameForFile: Boolean, successCallback: (newStr: String) -> Unit, failedCallback: (str: String) -> Unit = {}) {
        viewModelScope.launch {
            val parentPath = PathUtilities.removeLastSegment(renameCurrentPath)
            val newPath = if (renameForFile) {
                parentPath + "/" + renameNewPath + "." + PathUtilities.getFileExtension(renameCurrentPath)
            } else {
                "$parentPath/$renameNewPath"
            }

            if (renameForFile) {
                val response = filesFolderRepository.renameFile(renameCurrentPath, newPath)
                if (response.status == ResponseStatus.SUCCESSFUL) {
                    toastHelper.makeToast(response.message)
                    successCallback(response.message)
                    return@launch
                }

                toastHelper.makeToast(response.message)
                failedCallback(response.message)
            }
        }
    }
}