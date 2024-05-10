package com.example.ocr_digital.file_saver

import com.google.firebase.Timestamp

data class FileMetadata(
    var title: String,
    val author: String,
    val genre: String,
    val publishedYear: String,
    val path: String,
    val createdAt: Timestamp
) {
    constructor() : this("","","","","", Timestamp.now())
}
