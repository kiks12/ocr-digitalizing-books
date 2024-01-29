package com.example.ocr_digital.home

import com.google.firebase.storage.StorageReference

data class HomeState(
    val folders : List<StorageReference>,
//    val backupFolders : List<StorageReference>,
    val files : List<StorageReference>,
//    val backupFiles : List<StorageReference>,
    val loading : Boolean,
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
    val dialogLoading : Boolean,
    val query : String,
)