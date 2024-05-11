package com.example.ocr_digital.folder

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.file_saver.FileMetadata
import com.example.ocr_digital.image_scanner.ImageScannerActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.example.ocr_digital.translator.TranslatorActivity
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FolderUtilityViewModel(
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {

    private val filesFolderRepository = FilesFolderRepository()

    fun searchFile(path: String, query: String, onSearchFiles: (files: List<StorageReference>) -> Unit) {
        Log.w("FOLDER UTILITY", path)
        viewModelScope.launch {
            filesFolderRepository.searchFiles(path, query, onSearchFiles)
        }
    }

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

    fun getFileDetails(path: String, onFileMetadataChange: (metadata: FileMetadata) -> Unit, callback: () -> Unit) {
        viewModelScope.launch {
            val result = filesFolderRepository.getFileMetadata(path)
            val metadata = result.data["metadata"] as FileMetadata
            val metadataTwo = metadata.copy(path = path)
            onFileMetadataChange(metadataTwo)
            callback()
        }
    }

    fun updateFileDetails(metadata: FileMetadata, callback: () -> Unit) {
        viewModelScope.launch {
            val result = filesFolderRepository.uploadFileMetadata(metadata)
            toastHelper.makeToast(result.message)
            callback()
        }
    }

    fun deleteFileOrFolder(fileFolderPath: String, forFile: Boolean, successCallback: (newStr: String) -> Unit, failedCallback: (str: String) -> Unit = {}) {
        viewModelScope.launch {
            if (forFile) {
                val response = filesFolderRepository.deleteFile(fileFolderPath.substring(1))
                val responseTwo = filesFolderRepository.deleteFileMetadata(fileFolderPath)
                if (response.status == ResponseStatus.SUCCESSFUL && responseTwo.status == ResponseStatus.SUCCESSFUL) {
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
            toastHelper.makeToast("File type cannot be printed, download the file and use third party app")
            return
        }
        viewModelScope.launch {
            filesFolderRepository.printFile(context, path)
        }
    }

    suspend fun translateFile(filePath: String, folderPath: String) {
        val removedExtFilePath  = filePath.substringBeforeLast(".")
        val textFile = "$removedExtFilePath.txt"
        viewModelScope.launch {
            val fileContent = filesFolderRepository.readTextFromTxtFile(textFile)
            activityStarterHelper.startActivity(
                TranslatorActivity::class.java,
                stringExtras = mapOf(
                    "FILE_CONTENT" to fileContent,
                    "FILE_PATH" to filePath,
                    "FOLDER_PATH" to folderPath
                )
            )
        }
    }
}