package com.example.ocr_digital.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.repositories.FilesFolderRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val filesFolderRepository = FilesFolderRepository()
    private val auth = Firebase.auth
    private val _state = mutableStateOf(
        HomeState(
            folders = listOf(),
            files = listOf()
        )
    )

    val state : HomeState
        get() = _state.value

    init {
        viewModelScope.launch {
            val response = filesFolderRepository.getFilesAndFolders(auth.currentUser?.uid!!)
            val files = (response.data["FILES"] as List<*>).map { file -> file.toString() }
            val folders = (response.data["FOLDERS"] as List<*>).map { folder -> folder.toString() }
            _state.value = _state.value.copy(
                files = files,
                folders = folders
            )
        }
    }
}