package com.example.ocr_digital.models

import com.google.firebase.storage.StorageReference

data class SearchFilesFoldersResult(
    val files : List<StorageReference>,
    val folders : List<StorageReference>
)
