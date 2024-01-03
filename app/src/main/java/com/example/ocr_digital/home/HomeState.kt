package com.example.ocr_digital.home

import com.google.firebase.storage.StorageReference

data class HomeState(
    val folders : List<StorageReference>,
    val files : List<StorageReference>,
    val folderName : String,
    val showBottomSheet : Boolean,
    val showCreateFolderDialog : Boolean,
    val showDeleteFileOrFolderDialog : Boolean,
    val showRenameFileOrFolderDialog : Boolean,
    val renameForFile : Boolean,
    val renameCurrentPath : String,
    val renameNewPath : String,
    val fileOrFolderPath : String,
)