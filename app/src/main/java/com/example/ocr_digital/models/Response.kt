package com.example.ocr_digital.models

enum class ResponseStatus {
    SUCCESSFUL,
    FAILED
}

data class Response(
    val status: ResponseStatus,
    val message: String,
    val data: Map<String, Any> = mapOf()
)
