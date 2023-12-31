package com.example.ocr_digital.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.components.ActionsBottomSheet
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.components.dialogs.CreateFolderDialog
import com.example.ocr_digital.components.dialogs.DeleteFileFolderDialog
import com.example.ocr_digital.components.dialogs.RenameDialog
import com.example.ocr_digital.folder.FolderUtilityViewModel
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val state = homeViewModel.state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = homeViewModel::showBottomSheet) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            SearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
                active = false,
                onActiveChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                placeholder = { Text(text = "Search File") },
                trailingIcon = {
                   Icon(Icons.Default.Search, "Search")
                }
            ) {
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(25.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(text = "Folders")
            }
            items(state.folders) {folder ->
                Folder(
                    directoryName = folder.name,
                    onDeleteClick = { homeViewModel.showDeleteFileOrFolderDialog(folder.path) },
                    onRenameClick = { homeViewModel.showRenameFileOrFolderDialog(folder.path) },
                    onMoveClick = {},
                    onFolderClick = { homeViewModel.openFolder(folder.path) }
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(text = "Files")
            }
            items(state.files) {file ->
                File(
                    filename = file.name,
                    onDeleteClick = { homeViewModel.showDeleteFileOrFolderDialog(file.path, forFile = true) },
                    onRenameClick = { homeViewModel.showRenameFileOrFolderDialog(file.path, forFile = true) },
                    onMoveClick = {}
                )
            }
        }

        if (state.showBottomSheet) {
            ActionsBottomSheet(
                sheetState = sheetState,
                onDismissRequest = homeViewModel::hideBottomSheet,
                scanText = { folderUtilityViewModel.scanText("/${homeViewModel.getUid()}") },
                createFolder = homeViewModel::showCreateFolderDialog
            )
        }

        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.folderName,
                onFolderNameChange = homeViewModel::onFolderNameChange,
                onDismissRequest = homeViewModel::hideCreateFolderDialog,
                onCreateClick = {
                    folderUtilityViewModel.createFolder(
                        folderName = state.folderName,
                        folderPath = "/${homeViewModel.getUid()}",
                        successCallback = {
                            homeViewModel.refresh()
                            homeViewModel.hideBottomSheet()
                            homeViewModel.hideCreateFolderDialog()
                        },
                    )
                }
            )
        }

        if (state.showDeleteFileOrFolderDialog) {
            DeleteFileFolderDialog(
                loading = state.dialogLoading,
                onDismissRequest = homeViewModel::hideDeleteFileOrFolderDialog,
                onDeleteClick = {
                    homeViewModel.showDialogLoader()
                    folderUtilityViewModel.deleteFileOrFolder(
                        fileFolderPath = state.fileOrFolderPath,
                        forFile = state.deleteForFile,
                        successCallback = {
                            homeViewModel.hideDialogLoader()
                            homeViewModel.refresh()
                            homeViewModel.hideBottomSheet()
                            homeViewModel.hideDeleteFileOrFolderDialog()
                        },
                        failedCallback = {
                            homeViewModel.hideDialogLoader()
                        }
                    )
                }
            )
        }

        if (state.showRenameFileOrFolderDialog) {
            RenameDialog(
                forFile = state.renameForFile,
                loading = state.dialogLoading,
                onDismissRequest = homeViewModel::hideRenameFileOrFolderDialog,
                value = state.renameNewPath,
                onValueChange = homeViewModel::onRenameNewPathChange,
                onRenameClick = {
                    homeViewModel.showDialogLoader()
                    folderUtilityViewModel.renameFileOrFolder(
                        renameCurrentPath = state.renameCurrentPath,
                        renameNewPath = state.renameNewPath,
                        renameForFile = state.renameForFile,
                        successCallback = {
                            homeViewModel.hideDialogLoader()
                            homeViewModel.refresh()
                            homeViewModel.hideBottomSheet()
                            homeViewModel.hideRenameFileOrFolderDialog()
                        },
                        failedCallback = {
                            homeViewModel.hideDialogLoader()
                        }
                    )
                }
            )
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    val toastHelper = ToastHelper(LocalContext.current)
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    HomeScreen(
        homeViewModel = HomeViewModel(activityStarterHelper),
        folderUtilityViewModel = FolderUtilityViewModel(toastHelper, activityStarterHelper)
    )
}