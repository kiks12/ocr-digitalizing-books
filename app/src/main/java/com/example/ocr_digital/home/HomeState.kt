package com.example.ocr_digital.home

import com.google.firebase.storage.StorageReference

data class HomeState(
    val folders : List<StorageReference>,
    val files : List<StorageReference>,
    val folderName : String,
    val showBottomSheet : Boolean,
    val showCreateFolderDialog : Boolean,
    val showDeleteFileOrFolderDialog : Boolean,
    val fileOrFolderPath : String,
)