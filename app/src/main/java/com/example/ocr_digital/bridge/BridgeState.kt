package com.example.ocr_digital.bridge

import com.example.ocr_digital.file_saver.FileType

data class BridgeState (
    val text : String,
    val showSaveDialog : Boolean,
    val filename : String,
    val filetype : FileType,
    val showResetDialog : Boolean
)
