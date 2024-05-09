package com.example.ocr_digital.scan

import com.google.firebase.storage.StorageReference

data class ScanState(
    val folders : List<StorageReference>,
    val files : List<StorageReference>,
    val loading : Boolean,
    val folderName : String,
    val filePath : String,
    val showCopyToDialog : Boolean,
    val showCreateFolderDialog : Boolean,
    val showDeleteFileOrFolderDialog : Boolean,
    val deleteForFile : Boolean,
    val showRenameFileOrFolderDialog : Boolean,
    val renameForFile : Boolean,
    val renameCurrentPath : String,
    val renameNewPath : String,
    val fileOrFolderPath : String,
    val dialogLoading : Boolean,
    val query : String,
    val publicAdminUID : String,
    val parentFolder : String,
    val selectedFolder : String,
    val copyToFolders : List<StorageReference>,
)