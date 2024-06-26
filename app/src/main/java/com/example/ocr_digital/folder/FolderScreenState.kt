package com.example.ocr_digital.folder

import com.example.ocr_digital.file_saver.FileMetadata
import com.google.firebase.storage.StorageReference

data class FolderScreenState(
    val name: String,
    val folders : List<StorageReference>,
    val loading : Boolean,
    val files : List<StorageReference>,
    val filePath : String,
    val showCopyToDialog : Boolean,
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
    val publicAdminUID : String,
    val parentFolder : String,
    val selectedFolder : String,
    val copyToFolders : List<StorageReference>,
    val showInfoDialog : Boolean,
    val metadata: FileMetadata,
)
