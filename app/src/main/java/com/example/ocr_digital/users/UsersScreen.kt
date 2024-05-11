package com.example.ocr_digital.users

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.components.dialogs.EditUserDialog
import com.example.ocr_digital.components.dialogs.EnableDisableConfirmationDialog
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(usersViewModel: UsersViewModel) {
    val state = usersViewModel.state
    val refreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = usersViewModel::refresh)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Users", fontWeight = FontWeight.SemiBold) },
                actions = {
                    IconButton(onClick = usersViewModel::refresh ) {
                        Icon(Icons.Default.Refresh, "Refresh")
                    }
                }
            )
        }
    ){ innerPadding ->
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .pullRefresh(refreshState)
            ){
                item {
                    SearchBar(
                        query = state.query,
                        onQueryChange = usersViewModel::onQueryChange,
                        onSearch = { usersViewModel.searchUser() },
                        active = false,
                        onActiveChange = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        placeholder = { Text(text = "Search User") },
                        trailingIcon = {
                            Icon(Icons.Default.Search, "Search")
                        }
                    ) {}
                }
                itemsIndexed(state.users) {index: Int,  item: UserResult->
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = if (item.disabled) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                        ),
                        leadingContent = { Text(text = "${index+1}") },
                        headlineContent = { Text(text = "${item.firstName} ${item.lastName} ${if (item.admin) "- ADMIN" else ""}", fontSize = 14.sp, fontWeight = FontWeight.SemiBold) },
                        supportingContent = {
                            Column {
                                Text(text = item.email, fontSize = 9.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                                Text(text = if (item.disabled) "Disabled" else "Enabled", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSecondaryContainer)
                            }
                        },
                        trailingContent = {
                            var showDropDown by remember { mutableStateOf(false) }
                            Box {
                                IconButton(onClick = { showDropDown = !showDropDown }) {
                                    Icon(Icons.Default.MoreVert, "More")
                                }
                                DropdownMenu(expanded = showDropDown, onDismissRequest = { showDropDown = false }) {
                                    DropdownMenuItem(text = { Text(text = "Edit") }, onClick = { usersViewModel.onEditClick(item.uid) })
                                    DropdownMenuItem(text = { Text(text = "Delete") }, onClick = { usersViewModel.onDeleteClick(item.uid) })
                                    if (item.disabled) {
                                        DropdownMenuItem(text = { Text(text = "Enable") }, onClick = { usersViewModel.onEnableClick(item.uid) })
                                    } else {
                                        DropdownMenuItem(text = { Text(text = "Disable") }, onClick = { usersViewModel.onDisableClick(item.uid) })
                                    }
                                }
                            }
                        }
                    )
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = refreshState,
                )
            }

            if (state.showEditUserDialog) {
                EditUserDialog(
                    loading = state.editUserDialogLoading,
                    firstName = state.firstName,
                    onFirstNameChange = usersViewModel::onFirstNameChange,
                    lastName = state.lastName,
                    onLastNameChange = usersViewModel::onLastNameChange,
                    contactNumber = state.contactNumber,
                    onContactNumberChange = usersViewModel::onContactNumberChange,
                    admin = state.admin,
                    onAdminChange = usersViewModel::onAdminChange,
                    onDismissRequest = usersViewModel::onEditUserDialogDismiss,
                    update = usersViewModel::updateUserProfile
                )
            }

            if (state.showDeleteConfirmationDialog) {
                EnableDisableConfirmationDialog(
                    loading = state.deleteConfirmationLoading,
                    prompt = "Are you sure you want delete this account?",
                    onDismissRequest = usersViewModel::onDeleteConfirmationDialogDismiss,
                    callback = usersViewModel::deleteUser
                )
            }

            if (state.showDisableConfirmationDialog) {
                EnableDisableConfirmationDialog(
                    loading = state.disableConfirmationLoading,
                    prompt = "Are you sure you want disable this account?",
                    onDismissRequest = usersViewModel::onDisableConfirmationDialogDismiss,
                    callback = usersViewModel::disableUser
                )
            }

            if (state.showEnableConfirmationDialog) {
                EnableDisableConfirmationDialog(
                    loading = state.enableConfirmationLoading,
                    prompt = "Are you sure you want enable this account?",
                    onDismissRequest = usersViewModel::onEnableConfirmationDialogDismiss,
                    callback = usersViewModel::enableUser
                )
            }
        }
    }
}