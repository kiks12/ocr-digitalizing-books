package com.example.ocr_digital.image_scanner

import com.example.ocr_digital.file_saver.FileType

data class ImageScannerState (
    val text : String,
    val showSaveDialog : Boolean,
    val filename : String,
    val filetype : FileType,
    val showResetDialog : Boolean,
    val loading : Boolean
)
