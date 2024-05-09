package com.example.ocr_digital.users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.repositories.UsersRepository
import kotlinx.coroutines.launch

class UsersViewModel : ViewModel() {

    private val usersRepository = UsersRepository()

    private val _state = mutableStateOf(
        UsersState(
            users = listOf(),
            usersBackup = listOf(),
            query = ""
        )
    )

    val state : UsersState
        get() = _state.value

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val response = usersRepository.getUsers()
            val users = response.data.get("users") as List<UserInformation>
            _state.value = _state.value.copy(users = users, usersBackup = users)
        }
    }

    fun onQueryChange(newString: String) { _state.value = _state.value.copy(query = newString) }

    fun searchUser() {
        val q = _state.value.query.lowercase()
        _state.value = _state.value.copy(
            users = _state.value.usersBackup.filter {
                userInformation -> userInformation.firstName.lowercase().contains(q) || userInformation.lastName.lowercase().contains(q)
            }
        )
    }
}