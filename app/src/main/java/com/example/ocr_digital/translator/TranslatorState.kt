package com.example.ocr_digital.translator

data class TranslatorState(
    val sourceDropDownExpanded: Boolean,
    val sourceDropDownSelectedText: String,
    val targetDropDownExpanded: Boolean,
    val targetDropDownSelectedText: String,
    val text: String,
    val loading: Boolean,
)
