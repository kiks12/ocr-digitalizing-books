package com.example.ocr_digital.home

data class HomeState (
    val authenticated : Boolean,
    val displayName : String,
    val message : String
)