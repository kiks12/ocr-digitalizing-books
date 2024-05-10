package com.example.ocr_digital.folder

import android.webkit.MimeTypeMap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.components.dialogs.CopyToDialog
import com.example.ocr_digital.components.dialogs.CreateFolderDialog
import com.example.ocr_digital.components.dialogs.DeleteFileFolderDialog
import com.example.ocr_digital.components.dialogs.FileMetadataDialog
import com.example.ocr_digital.components.dialogs.RenameDialog
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.path.PathUtilities
import compose.icons.FeatherIcons
import compose.icons.feathericons.FileText
import compose.icons.feathericons.FolderPlus
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FolderScreen(folderViewModel: FolderViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
    val state = folderViewModel.state
    val scope = rememberCoroutineScope()
    val localContext = LocalContext.current
    val refreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = folderViewModel::refresh)

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                FloatingActionButton(
                    onClick = folderViewModel::showCreateFolderDialog,
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.FolderPlus, contentDescription = "New Folder")
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("Add Folder", fontSize = 11.sp)
                Spacer(modifier = Modifier.height(10.dp))
                FloatingActionButton(
                    onClick = { folderUtilityViewModel.scanText(folderViewModel.getFolderPath()) },
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.FileText, contentDescription = "Scanner")
                }
                Spacer(modifier = Modifier.height(5.dp))
                Text("Scanner", fontSize = 11.sp)
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
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        else {
            if (state.folders.isEmpty() && state.files.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No folder and files saved")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxWidth()
                        .pullRefresh(refreshState)
                ) {
                    item { Text(text = "Saved Files", modifier = Modifier.padding(start = 15.dp, top=15.dp), fontSize = 12.sp) }
                    items(state.folders) {folder ->
                        Folder(
                            directoryName = folder.name,
                            onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(folder.path) },
                            onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(folder.path) },
                            onMoveClick = {},
                            onFolderClick = { folderViewModel.openFolder(folder.path) },
                            authenticated = folderViewModel.isAuthenticated(),
                        )
                    }
                    items(state.files) {file ->
                        File(
                            filename = file.name,
                            onDeleteClick = { folderViewModel.showDeleteFileOrFolderDialog(file.path, forFile = true) },
                            onRenameClick = { folderViewModel.showRenameFileOrFolderDialog(file.path, forFile = true) },
                            onCopyClick = { folderViewModel.showCopyToDialog(file.path) },
                            onDownloadClick = { folderUtilityViewModel.downloadFile(localContext, file.path) },
                            onPrintClick = { folderUtilityViewModel.printFile(localContext, file.path) },
                            onDetailsClick = { folderUtilityViewModel.getFileDetails(file.path, folderViewModel::onFileMetadataChange, folderViewModel::showFileMetadataDialog) },
                            onTranslateClick = {
                                scope.launch {
                                    folderUtilityViewModel.translateFile(file.path, folderViewModel.getFolderPath())
                                }
                            },
                            onClick = {
                                val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(PathUtilities.getFileExtension(file.path)) ?: ""
                                folderUtilityViewModel.onFileClick(localContext, file.path, mimetype)
                            },
                            authenticated = folderViewModel.isAuthenticated(),
                        )
                    }
                }
            }
        }

        if (state.showInfoDialog) {
            FileMetadataDialog(
                metadata = state.metadata,
                onDismissRequest = folderViewModel::hideFileMetadataDialog,
                update = { folderUtilityViewModel.updateFileDetails(it, folderViewModel::hideFileMetadataDialog) }
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

        if (state.showCopyToDialog) {
            CopyToDialog(
                folders = state.copyToFolders,
                parentFolder = state.parentFolder,
                selectedFolder = state.selectedFolder,
                setSelectedFolder = folderViewModel::setSelectedFolder,
                showCreateFolderDialog = folderViewModel::showCreateFolderDialog,
                onDismissRequest = folderViewModel::hideCopyToDialog,
                onSave = { folderViewModel.copyFileToSelectedFolder() }
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
    val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

    FolderScreen(folderViewModel, folderUtilityViewModel)
}