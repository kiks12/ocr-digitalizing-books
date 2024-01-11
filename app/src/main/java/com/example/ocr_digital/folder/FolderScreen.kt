package com.example.ocr_digital.folder

import android.webkit.MimeTypeMap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ocr_digital.components.ActionsBottomSheet
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.components.dialogs.CreateFolderDialog
import com.example.ocr_digital.components.dialogs.DeleteFileFolderDialog
import com.example.ocr_digital.components.dialogs.RenameDialog
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.path.PathUtilities

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(folderViewModel: FolderViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
    val state = folderViewModel.state
    val sheetState = rememberModalBottomSheetState()
    val localContext = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = folderViewModel::showBottomSheet,
                shape = CircleShape
            ) {
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
                .fillMaxWidth()
        ) {
            if (state.loading) {
                item {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else {
                items(state.folders) {folder ->
                    Folder(
                        directoryName = folder.name,
                        onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(folder.path) },
                        onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(folder.path) },
                        onMoveClick = {},
                        onFolderClick = { folderViewModel.openFolder(folder.path) },
                    )
                }
                items(state.files) {file ->
                    File(
                        filename = file.name,
                        onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(file.path, forFile = true) },
                        onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(file.path, forFile = true) },
                        onMoveClick = {},
                        onDownloadClick = { folderUtilityViewModel.downloadFile(localContext, file.path) },
                        onPrintClick = { folderUtilityViewModel.printFile(localContext, file.path) },
                        onClick = {
                            val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(PathUtilities.getFileExtension(file.path)) ?: ""
                            folderUtilityViewModel.onFileClick(localContext, file.path, mimetype)
                        }
                    )
                }
            }
        }

        if (state.showBottomSheet) {
            ActionsBottomSheet(
                sheetState = sheetState,
                onDismissRequest = folderViewModel::hideBottomSheet,
                scanText = { folderUtilityViewModel.scanText(folderViewModel.getFolderPath()) },
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
                        },
                    )
                }
            )
        }

        if (state.showDeleteFileOrFolderDialog) {
            DeleteFileFolderDialog(
                loading = state.dialogLoading,
                onDismissRequest = folderViewModel::hideDeleteFileOrFolderDialog,
                onDeleteClick = {
                    folderViewModel.showDialogLoader()
                    folderUtilityViewModel.deleteFileOrFolder(
                        fileFolderPath = state.fileOrFolderPath,
                        forFile = state.deleteForFile,
                        successCallback = {
                            folderViewModel.hideDialogLoader()
                            folderViewModel.hideBottomSheet()
                            folderViewModel.hideDeleteFileOrFolderDialog()
                            folderViewModel.refresh()
                        },
                        failedCallback = {
                            folderViewModel.hideDialogLoader()
                        }
                    )
                }
            )
        }

        if (state.showRenameFileOrFolderDialog) {
            RenameDialog(
                forFile = state.renameForFile,
                loading = state.dialogLoading,
                onDismissRequest = folderViewModel::hideRenameFileOrFolderDialog,
                value = state.renameNewPath,
                onValueChange = folderViewModel::onRenameNewPathChange,
                onRenameClick = {
                    folderViewModel.showDialogLoader()
                    folderUtilityViewModel.renameFileOrFolder(
                        renameCurrentPath = state.renameCurrentPath,
                        renameNewPath = state.renameNewPath,
                        renameForFile = state.renameForFile,
                        successCallback = {
                            folderViewModel.hideDialogLoader()
                            folderViewModel.hideBottomSheet()
                            folderViewModel.hideRenameFileOrFolderDialog()
                            folderViewModel.refresh()
                        },
                        failedCallback = {
                            folderViewModel.hideDialogLoader()
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
        activityStarterHelper = activityStarterHelper
    ) {}
    val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

    FolderScreen(folderViewModel, folderUtilityViewModel)
}