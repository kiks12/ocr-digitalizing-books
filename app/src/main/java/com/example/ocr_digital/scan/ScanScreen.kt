package com.example.ocr_digital.scan

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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.components.dialogs.CreateFolderDialog
import com.example.ocr_digital.components.dialogs.DeleteFileFolderDialog
import com.example.ocr_digital.components.dialogs.RenameDialog
import com.example.ocr_digital.folder.FolderUtilityViewModel
import com.example.ocr_digital.path.PathUtilities
import compose.icons.FeatherIcons
import compose.icons.feathericons.FileText
import compose.icons.feathericons.FolderPlus
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScanScreen(scanViewModel: ScanViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
//    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val state = scanViewModel.state
    val localContext = LocalContext.current
    val refreshing by remember { mutableStateOf(false) }
    val refreshState = rememberPullRefreshState(refreshing = refreshing, onRefresh = scanViewModel::refresh)

    Scaffold(
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                FloatingActionButton(
                    onClick = scanViewModel::showCreateFolderDialog,
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.FolderPlus, contentDescription = "New Folder")
                }
                Spacer(modifier = Modifier.height(10.dp))
                FloatingActionButton(
                    onClick = { folderUtilityViewModel.scanText("/${scanViewModel.getUid()}") },
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.FileText, contentDescription = "Scanner")
                }
            }
        },
        topBar = {
            SearchBar(
                query = state.query,
                onQueryChange = scanViewModel::onQueryChange,
                onSearch = { scanViewModel.searchFile() },
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
        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
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
                    item { Text(text = "Saved Folders", modifier = Modifier.padding(start = 15.dp, top=15.dp), fontSize = 12.sp) }
                    items(state.folders) {folder ->
                        Folder(
                            directoryName = folder.name,
                            onDeleteClick = { scanViewModel.showDeleteFileOrFolderDialog(folder.path) },
                            onRenameClick = { scanViewModel.showRenameFileOrFolderDialog(folder.path) },
                            onMoveClick = {},
                            onFolderClick = { scanViewModel.openFolder(folder.path) },
                        )
                    }
                    item { Text(text = "Saved Files", modifier = Modifier.padding(start = 15.dp, top=15.dp), fontSize = 12.sp) }
                    items(state.files) {file ->
                        File(
                            filename = file.name,
                            onDeleteClick = { scanViewModel.showDeleteFileOrFolderDialog(file.path, forFile = true) },
                            onRenameClick = { scanViewModel.showRenameFileOrFolderDialog(file.path, forFile = true) },
                            onMoveClick = {},
                            onDownloadClick = { folderUtilityViewModel.downloadFile(localContext, file.path) },
                            onPrintClick = { folderUtilityViewModel.printFile(localContext, file.path) },
                            onTranslateClick = {
                                scope.launch {
                                    folderUtilityViewModel.translateFile(file.path, "/${scanViewModel.getUid()}")
                                }
                            },
                            onClick = {
                                val mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                                    PathUtilities.getFileExtension(file.path)) ?: ""
                                folderUtilityViewModel.onFileClick(localContext, file.path, mimetype)
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
            }
        }

//        if (state.showBottomSheet) {
//            ActionsBottomSheet(
//                sheetState = sheetState,
//                onDismissRequest = scanViewModel::hideBottomSheet,
//                scanText = { folderUtilityViewModel.scanText("/${scanViewModel.getUid()}") },
//                createFolder = scanViewModel::showCreateFolderDialog
//            )
//        }

        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.folderName,
                onFolderNameChange = scanViewModel::onFolderNameChange,
                onDismissRequest = scanViewModel::hideCreateFolderDialog,
                onCreateClick = {
                    folderUtilityViewModel.createFolder(
                        folderName = state.folderName,
                        folderPath = "/${scanViewModel.getUid()}",
                        successCallback = {
                            scanViewModel.refresh()
                            scanViewModel.hideBottomSheet()
                            scanViewModel.hideCreateFolderDialog()
                        },
                    )
                }
            )
        }

        if (state.showDeleteFileOrFolderDialog) {
            DeleteFileFolderDialog(
                loading = state.dialogLoading,
                onDismissRequest = scanViewModel::hideDeleteFileOrFolderDialog,
                onDeleteClick = {
                    scanViewModel.showDialogLoader()
                    folderUtilityViewModel.deleteFileOrFolder(
                        fileFolderPath = state.fileOrFolderPath,
                        forFile = state.deleteForFile,
                        successCallback = {
                            scanViewModel.hideDialogLoader()
                            scanViewModel.refresh()
                            scanViewModel.hideBottomSheet()
                            scanViewModel.hideDeleteFileOrFolderDialog()
                        },
                        failedCallback = {
                            scanViewModel.hideDialogLoader()
                        }
                    )
                }
            )
        }

        if (state.showRenameFileOrFolderDialog) {
            RenameDialog(
                forFile = state.renameForFile,
                loading = state.dialogLoading,
                onDismissRequest = scanViewModel::hideRenameFileOrFolderDialog,
                value = state.renameNewPath,
                onValueChange = scanViewModel::onRenameNewPathChange,
                onRenameClick = {
                    scanViewModel.showDialogLoader()
                    folderUtilityViewModel.renameFileOrFolder(
                        renameCurrentPath = state.renameCurrentPath,
                        renameNewPath = state.renameNewPath,
                        renameForFile = state.renameForFile,
                        successCallback = {
                            scanViewModel.hideDialogLoader()
                            scanViewModel.refresh()
                            scanViewModel.hideBottomSheet()
                            scanViewModel.hideRenameFileOrFolderDialog()
                        },
                        failedCallback = {
                            scanViewModel.hideDialogLoader()
                        }
                    )
                }
            )
        }
    }
}