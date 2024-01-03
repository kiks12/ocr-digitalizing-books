package com.example.ocr_digital.folder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(folderViewModel: FolderViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
    val state = folderViewModel.state
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = folderViewModel::showBottomSheet) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(state.name) },
                navigationIcon = {
                    IconButton(onClick = folderViewModel::finish) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                }
            )
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
                    onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(folder.path) },
                    onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(folder.path) },
                    onMoveClick = {},
                    onFolderClick = { folderViewModel.openFolder(folder.path) }
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(text = "Files")
            }
            items(state.files) {file ->
                File(
                    filename = file.name,
                    onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(file.path) },
                    onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(file.path, forFile = true) },
                    onMoveClick = {}
                )
            }
        }

        if (state.showBottomSheet) {
            ActionsBottomSheet(
                sheetState = sheetState,
                onDismissRequest = folderViewModel::hideBottomSheet,
                useCamera = {},
                uploadImages = {},
                createFolder = folderViewModel::showCreateFolderDialog
            )
        }

        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.folderName,
                onFolderNameChange = folderViewModel::onFolderNameChange,
                onDismissRequest = folderViewModel::hideCreateFolderDialog,
                onCreateClick = {
                    folderUtilityViewModel.createFolder(
                        folderName = state.folderName,
                        folderPath = folderViewModel.getFolderPath(),
                        successCallback = {
                            folderViewModel.hideBottomSheet()
                            folderViewModel.hideCreateFolderDialog()
                            folderViewModel.refresh()
                            folderViewModel.getToastHelper().makeToast(it)
                        },
                        failedCallback = {
                            folderViewModel.getToastHelper().makeToast(it)
                        }
                    )
                }
            )
        }

        if (state.showDeleteFileOrFolderDialog) {
            DeleteFileFolderDialog(
                onDismissRequest = folderViewModel::hideDeleteFileOrFolderDialog,
                onDeleteClick = {
                    folderUtilityViewModel.deleteFileOrFolder(
                        fileFolderPath = state.fileOrFolderPath,
                        successCallback = {
                            folderViewModel.hideBottomSheet()
                            folderViewModel.hideDeleteFileOrFolderDialog()
                            folderViewModel.refresh()
                            folderViewModel.getToastHelper().makeToast(it)
                        }
                    )
                }
            )
        }

        if (state.showRenameFileOrFolderDialog) {
            RenameDialog(
                forFile = state.renameForFile,
                onDismissRequest = folderViewModel::hideRenameFileOrFolderDialog,
                value = state.renameNewPath,
                onValueChange = folderViewModel::onRenameNewPathChange,
                onRenameClick = {
                    folderUtilityViewModel.renameFileOrFolder(
                        renameCurrentPath = state.renameCurrentPath,
                        renameNewPath = state.renameNewPath,
                        renameForFile = state.renameForFile,
                        successCallback = {
                            folderViewModel.hideBottomSheet()
                            folderViewModel.hideRenameFileOrFolderDialog()
                            folderViewModel.refresh()
                            folderViewModel.getToastHelper().makeToast(it)
                        }
                    )
                }
            )
        }
    }
}


@Preview
@Composable
fun FolderScreenPreview() {
    val toastHelper = ToastHelper(LocalContext.current)
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    val folderViewModel = FolderViewModel(
        "Try",
        toastHelper = toastHelper,
        activityStarterHelper = activityStarterHelper
    ) {}
    val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper)

    FolderScreen(folderViewModel, folderUtilityViewModel)
}