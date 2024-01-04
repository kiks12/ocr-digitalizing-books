package com.example.ocr_digital.folder

import com.google.firebase.storage.StorageReference

data class FolderScreenState(
    val name: String,
    val folders : List<StorageReference>,
    val files : List<StorageReference>,
    val folderName : String,
    val showBottomSheet : Boolean,
    val showCreateFolderDialog : Boolean,
    val showDeleteFileOrFolderDialog : Boolean,
    val deleteForFile : Boolean,
    val showRenameFileOrFolderDialog : Boolean,
    val renameForFile : Boolean,
    val renameCurrentPath : String,
    val renameNewPath : String,
    val fileOrFolderPath : String,
    val dialogLoading : Boolean
)
