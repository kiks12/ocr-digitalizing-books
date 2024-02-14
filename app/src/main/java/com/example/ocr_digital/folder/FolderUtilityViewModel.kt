package com.example.ocr_digital.folder

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.image_scanner.ImageScannerActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.example.ocr_digital.translator.TranslatorActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FolderUtilityViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
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

    fun deleteFileOrFolder(fileFolderPath: String, forFile: Boolean, successCallback: (newStr: String) -> Unit, failedCallback: (str: String) -> Unit = {}) {
        viewModelScope.launch {
            if (forFile) {
                val response = filesFolderRepository.deleteFile(fileFolderPath.substring(1))
                if (response.status == ResponseStatus.SUCCESSFUL) {
                    toastHelper.makeToast(response.message)
                    successCallback(response.message)
                    return@launch
                }

                toastHelper.makeToast(response.message)
                failedCallback(response.message)
                return@launch
            }

            val response = filesFolderRepository.deleteFolder(fileFolderPath)
            if (response.status == ResponseStatus.SUCCESSFUL) {
                delay(5000)
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
            } else {
                val response = filesFolderRepository.renameFolder(renameCurrentPath, newPath)
                if (response.status == ResponseStatus.SUCCESSFUL) {
                    delay(5000)
                    toastHelper.makeToast(response.message)
                    successCallback(response.message)
                    return@launch
                }

                toastHelper.makeToast(response.message)
                failedCallback(response.message)
            }
        }
    }

    fun scanText(path: String) {
        activityStarterHelper.startActivity(
            ImageScannerActivity::class.java,
            stringExtras = mapOf(
                "PATH" to path
            )
        )
    }

    fun onFileClick(context: Context, path: String, mimetype: String) {
        viewModelScope.launch {
            if (mimetype.isEmpty()) {
                toastHelper.makeToast("Invalid mimetype")
                return@launch
            }

            if (path.isEmpty()) {
                toastHelper.makeToast("Invalid file path")
                return@launch
            }

            filesFolderRepository.openFirebaseDocument(context, path, mimetype)
        }
    }

    fun downloadFile(context: Context, path: String) {
        viewModelScope.launch {
            if (path.isEmpty()) {
                toastHelper.makeToast("Invalid file path")
                return@launch
            }

            filesFolderRepository.downloadFile(context, path)
        }
    }

    fun printFile(context: Context, path: String) {
        val extension = PathUtilities.getFileExtension(path)
        if (extension.contains("png") || extension.contains("jpg") || extension.contains("docx")) {
            toastHelper.makeToast("File type cannot be printed, download the file and use third pary app")
            return
        }
        viewModelScope.launch {
            filesFolderRepository.printFile(context, path)
        }
    }

    fun translateFile(context: Context, path: String) {
        activityStarterHelper.startActivity(TranslatorActivity::class.java)
    }
}