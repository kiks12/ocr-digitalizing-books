package com.example.ocr_digital.translator

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.file_saver.FileSaver
import com.example.ocr_digital.file_saver.FileType
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.path.PathUtilities
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TranslatorViewModel(
    val text: String,
    private val filePath: String,
    private val folderPath: String,
    private val toastHelper: ToastHelper,
    private val finishCallback: () -> Unit,
) : ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val fileSaver = FileSaver()
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val languageIdentifier = LanguageIdentification.getClient()
    private val _state = mutableStateOf(
        TranslatorState(
            sourceDropDownExpanded = false,
            sourceDropDownSelectedText = "",
            targetDropDownExpanded = false,
            targetDropDownSelectedText = "",
            text = text,
            loading = false
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

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                val texts = _state.value.text.split("\n")
                Log.w("TRANSLATOR VIEW MODEL", texts.toString())
                _state.value = _state.value.copy(text = "")
                _state.value = _state.value.copy(loading = true)
                texts.forEach { text ->
                    scope.launch {
                        val translated = translator.translate(text).await()
                        if (_state.value.text == "") {
                            onTextChange(translated)
                        } else {
                            onTextChange(_state.value.text + "\n\n" + translated)
                        }
                    }
                }
                _state.value = _state.value.copy(loading = false)
            }
            .addOnFailureListener {
                it.localizedMessage?.let { it1 -> Log.w("TRANSLATOR VIEW MODEL", it1) }
            }

    }

    fun finish() {
        finishCallback()
    }

    fun save(context: Context) {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val extension = PathUtilities.getFileExtension(filePath)
            val type = when(extension) {
                "png" -> FileType.PNG
                "pdf" -> FileType.PDF
                "jpg" -> FileType.JPEG
                "docx" -> FileType.DOCX
                else -> FileType.TXT
            }
            val fileName = (PathUtilities.getLastSegment(filePath)).substringBeforeLast(".$extension") + "-${_state.value.targetDropDownSelectedText}"
            val uri = fileSaver.saveTextToFile(context, _state.value.text, fileName, type)
            val uriText = fileSaver.saveTextToFile(context, _state.value.text, fileName, FileType.TXT)
            if (uri != null && uriText != null) {
                val response = filesFolderRepository.uploadFile(folderPath, uri)
                val responseTwo = filesFolderRepository.uploadFile(folderPath, uriText)

                _state.value = _state.value.copy(loading = false)
                toastHelper.makeToast(response.message)
                toastHelper.makeToast(responseTwo.message)
            }
        }
    }
}