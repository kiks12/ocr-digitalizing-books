package com.example.ocr_digital.translator

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ocr_digital.helpers.ToastHelper
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class TranslatorViewModel(
    val text: String,
    private val toastHelper: ToastHelper,
) : ViewModel() {
    private val languageIdentifier = LanguageIdentification.getClient()
    private val _state = mutableStateOf(
        TranslatorState(
            sourceDropDownExpanded = false,
            sourceDropDownSelectedText = "",
            targetDropDownExpanded = false,
            targetDropDownSelectedText = "",
            text = text
        )
    )
    val state : TranslatorState
        get() = _state.value

    init {
        languageIdentifier.identifyLanguage(text)
            .addOnSuccessListener { languageCode ->
                when (languageCode) {
                    "und" -> {
                        toastHelper.makeToast("Cannot identify source language")
                    }
                    "fil" -> {
                        onSourceDropDownSelectedTextChange("Filipino")
                        onTargetDropDownSelectedTextChange("English")
                    }
                    "en" -> {
                        onSourceDropDownSelectedTextChange("English")
                        onTargetDropDownSelectedTextChange("Filipino")
                    }
                }
            }
    }

    fun onSourceDropDownExpandedChange(newVal: Boolean) { _state.value = _state.value.copy(sourceDropDownExpanded = newVal) }
    fun onSourceDropDownSelectedTextChange(newStr: String) { _state.value = _state.value.copy(sourceDropDownSelectedText = newStr) }
    fun onTargetDropDownExpandedChange(newVal: Boolean) { _state.value = _state.value.copy(targetDropDownExpanded = newVal) }
    fun onTargetDropDownSelectedTextChange(newStr: String) { _state.value = _state.value.copy(targetDropDownSelectedText = newStr) }
    fun onTextChange(newStr: String) { _state.value = _state.value.copy(text = newStr) }

    fun translate() {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(if (_state.value.sourceDropDownSelectedText == "Filipino") TranslateLanguage.TAGALOG else TranslateLanguage.ENGLISH)
            .setTargetLanguage(if (_state.value.targetDropDownSelectedText == "Filipino") TranslateLanguage.TAGALOG else TranslateLanguage.ENGLISH)
            .build()
        val translator = Translation.getClient(options)
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        Log.w("TRANSLATOR VIEW MODEL", "BEGINNING...")
        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Log.w("TRANSLATOR VIEW MODEL", "MODEL DOWNLOADED")
                translator.translate(_state.value.text)
                    .addOnSuccessListener { text ->
                        Log.w("TRANSLATOR VIEW MODEL", "TEXT TRANSLATED")
                        Log.w("TRANSLATOR VIEW MODEL", text)
                        onTextChange(text)
                    }
                    .addOnFailureListener {
                        Log.w("TRANSLATOR VIEW MODEL",it.localizedMessage)
                    }
            }
            .addOnFailureListener {
                Log.w("TRANSLATOR VIEW MODEL",it.localizedMessage)
            }

    }

}