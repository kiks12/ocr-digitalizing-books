package com.example.ocr_digital.users

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ocr_digital.api.UsersAPI
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.registration.RegistrationActivity
import com.example.ocr_digital.repositories.UsersRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UsersViewModel(
    private val usersAPI: UsersAPI,
    private val toastHelper: ToastHelper,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {

    private val usersRepository = UsersRepository()

    private val _state = mutableStateOf(
        UsersState(
            users = listOf(),
            usersBackup = listOf(),
            query = "",
            firstName = "",
            lastName = "",
            contactNumber = "",
            uid = "",
            loading = false,
            showEditUserDialog = false,
            editUserDialogLoading = false,
            showEnableConfirmationDialog = false,
            enableConfirmationLoading = false,
            showDisableConfirmationDialog = false,
            disableConfirmationLoading = false,
            showDeleteConfirmationDialog = false,
            deleteConfirmationLoading = false,
            admin = false
        )
    )

    val state : UsersState
        get() = _state.value

    init {
        refresh()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        toastHelper.makeToast(throwable.message!!)
    }

    fun refresh() {
        viewModelScope.launch {
            async { _state.value = _state.value.copy(loading = true) }.await()
            async {
                val users = usersAPI.getUsers()
                val result = users.body()?.result
                _state.value = _state.value.copy(loading = false, users = result?: listOf(), usersBackup = result?: listOf())
            }.await()
        }
    }

    fun onQueryChange(newString: String) { _state.value = _state.value.copy(query = newString) }

    fun searchUser() {
        val q = _state.value.query.lowercase()
        _state.value = _state.value.copy(
            users = _state.value.usersBackup.filter {
                userInformation -> userInformation.email.lowercase().contains(q) || userInformation.uid.lowercase().contains(q)
            }
        )
    }

    fun onAdminChange(newVal: Boolean) { _state.value = _state.value.copy(admin = newVal) }
    fun onFirstNameChange(newString: String) { _state.value = _state.value.copy(firstName = newString)}
    fun onLastNameChange(newString: String) { _state.value = _state.value.copy(lastName = newString)}
    fun onContactNumberChange(newString: String) { _state.value = _state.value.copy(contactNumber = newString)}
    fun onEditUserDialogDismiss() { _state.value = _state.value.copy(showEditUserDialog = false) }
    fun onDeleteConfirmationDialogDismiss() { _state.value = _state.value.copy(showDeleteConfirmationDialog = false) }
    fun onDisableConfirmationDialogDismiss() { _state.value = _state.value.copy(showDisableConfirmationDialog = false) }
    fun onEnableConfirmationDialogDismiss() { _state.value = _state.value.copy(showEnableConfirmationDialog = false) }

    fun onEditClick(uid: String) {
        viewModelScope.launch(exceptionHandler) {
            val response = usersRepository.getUser(uid)
            val userInformation = response.data["user"] as List<UserInformation>
            _state.value = _state.value.copy(
                firstName = userInformation[0].firstName,
                lastName = userInformation[0].lastName,
                contactNumber = userInformation[0].contactNumber,
                admin = userInformation[0].admin,
                uid = uid,
                showEditUserDialog = true
            )
        }
    }

    fun onDisableClick(uid: String) {
        _state.value = _state.value.copy(showDisableConfirmationDialog = true, uid = uid)
    }
    fun onEnableClick(uid: String) {
        _state.value = _state.value.copy(showEnableConfirmationDialog = true, uid = uid)
    }
    fun onDeleteClick(uid: String) {
        _state.value = _state.value.copy(showDeleteConfirmationDialog = true, uid = uid)
    }

    fun updateUserProfile() {
        viewModelScope.launch(exceptionHandler) {
            async { _state.value = _state.value.copy(editUserDialogLoading = true) }.await()
            async {
                val response = usersAPI.editUserProfile(mapOf(
                    "uid" to _state.value.uid,
                    "firstName" to _state.value.firstName,
                    "lastName" to _state.value.lastName,
                    "contactNumber" to _state.value.contactNumber,
                    "admin" to _state.value.admin.toString()
                ))
                async { _state.value = _state.value.copy(editUserDialogLoading = false) }.await()
                toastHelper.makeToast(response.body()?.message!!)
            }.await()
            async {
                onEditUserDialogDismiss()
                refresh()
            }.await()
        }
    }

    fun deleteUser() {
        viewModelScope.launch(exceptionHandler) {
            async { _state.value = _state.value.copy(deleteConfirmationLoading = true) }.await()
            async {
                val response = usersAPI.deleteUser(_state.value.uid)
                async { _state.value = _state.value.copy(deleteConfirmationLoading= false) }.await()
                toastHelper.makeToast(response.body()?.message!!)
            }.await()
            onDeleteConfirmationDialogDismiss()
            refresh()
        }
    }

    fun addUser() {
        activityStarterHelper.startActivity(RegistrationActivity::class.java, stringExtras = mapOf(
            "forAdmin" to "true"
        ))
    }

    fun disableUser() {
        viewModelScope.launch(exceptionHandler) {
            async { _state.value = _state.value.copy(disableConfirmationLoading = true) }.await()
            async {
                val response = usersAPI.disableUserAccount(mapOf(
                    "uid" to _state.value.uid,
                ))
                async { _state.value = _state.value.copy(disableConfirmationLoading= false) }.await()
                toastHelper.makeToast(response.body()?.message!!)
            }.await()
            onDisableConfirmationDialogDismiss()
            refresh()
        }
    }

    fun enableUser() {
        viewModelScope.launch(exceptionHandler) {
            async { _state.value = _state.value.copy(enableConfirmationLoading = true) }.await()
            async {
                val response = usersAPI.enableUserAccount(mapOf(
                    "uid" to _state.value.uid,
                ))
                async { _state.value = _state.value.copy(enableConfirmationLoading= false) }.await()
                toastHelper.makeToast(response.body()?.message!!)
            }.await()
            onEnableConfirmationDialogDismiss()
            refresh()
        }
    }
}