package com.example.ocr_digital.image_scanner

import com.example.ocr_digital.file_saver.FileType
import com.google.firebase.storage.StorageReference

data class ImageScannerState (
    val text : String,
    val showSaveDialog : Boolean,
    val filename : String,
    val filetype : FileType,
    val showResetDialog : Boolean,
    val loading : Boolean,
    val parentFolder: String,
    val selectedFolder : String,
    val folders : List<StorageReference>,
    val showCreateFolderDialog : Boolean,
    val folderName : String,
)
