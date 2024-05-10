package com.example.ocr_digital.image_scanner

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.camera.CameraActivity
import com.example.ocr_digital.components.dialogs.CreateFolderDialog
import com.example.ocr_digital.components.dialogs.ResetFileDialog
import com.example.ocr_digital.components.dialogs.SaveFileDialog
import com.example.ocr_digital.components.plain_text.PlainTextEditor
import com.example.ocr_digital.folder.FolderUtilityViewModel
import com.example.ocr_digital.gallery.GalleryActivity
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import compose.icons.FeatherIcons
import compose.icons.feathericons.Camera
import compose.icons.feathericons.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageScannerScreen(imageScannerViewModel: ImageScannerViewModel, folderUtilityViewModel: FolderUtilityViewModel) {
    val state = imageScannerViewModel.state

    val localContext = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val text = result.data?.getStringExtra("ActivityResult") ?: ""
        if (state.text == "") {
            imageScannerViewModel.onTextChange(state.text + text)
        } else {
            imageScannerViewModel.onTextChange(state.text + "\n" + text)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val text = result.data?.getStringExtra("ActivityResult") ?: ""
        if (state.text == "") {
            imageScannerViewModel.onTextChange(state.text + text)
        } else {
            imageScannerViewModel.onTextChange(state.text + "\n" + text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = imageScannerViewModel::finish) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                },
                title = { Text(text = "Image Scanner") },
                actions = {
                    IconButton(onClick = imageScannerViewModel::showResetDialog) {
                        Icon(Icons.Default.Refresh, "Reset")
                    }
                    IconButton(onClick = imageScannerViewModel::showSaveDialog) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = { cameraLauncher.launch(Intent(localContext, CameraActivity::class.java)) },
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.Camera, "Camera")
                }
                Spacer(modifier = Modifier.height(15.dp))
                FloatingActionButton(
                    onClick = { galleryLauncher.launch(Intent(localContext, GalleryActivity::class.java)) },
                    shape = CircleShape
                ) {
                    Icon(FeatherIcons.Image, "Upload")
                }
            }
        }
    )
    { innerPadding ->
        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(15.dp)
            ){
                item {
                    PlainTextEditor(text = state.text, onTextChanged = imageScannerViewModel::onTextChange)
                }
            }
        }

        if (state.showSaveDialog) {
            SaveFileDialog(
                filename = state.filename,
                onFileNameChange = imageScannerViewModel::onFileNameChange,
                onFileTypeChange = imageScannerViewModel::onFileTypeChange,
                onDismissRequest = imageScannerViewModel::hideSaveDialog,
                onSave = { imageScannerViewModel.saveFile(localContext, state.text, state.filename, state.filetype) },
                selectedFolder = state.selectedFolder,
                parentFolder = state.parentFolder,
                setSelectedFolder = imageScannerViewModel::setSelectedFolder,
                folders = state.folders,
                showCreateFolderDialog = imageScannerViewModel::showCreateFolderDialog,
                bookTitle = state.bookTitle,
                onBookTitleChange = imageScannerViewModel::onBookTitleChange,
                author = state.author,
                onAuthorChange = imageScannerViewModel::onAuthorChange,
                publishYear = state.publishYear,
                onPublishYear = imageScannerViewModel::onPublishYearChange,
                genre = state.genre,
                onGenreChange = imageScannerViewModel::onGenreChange
            )
        }
        
        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.folderName,
                onFolderNameChange = imageScannerViewModel::onFolderNameChange,
                onDismissRequest = imageScannerViewModel::hideCreateFolderDialog) {
                folderUtilityViewModel.createFolder(
                    folderName = state.folderName,
                    folderPath = state.selectedFolder,
                    successCallback = {
                        imageScannerViewModel.hideCreateFolderDialog()
                        imageScannerViewModel.getFolders()
                    },
                    failedCallback = {}
                )
            }
        }

        if (state.showResetDialog) {
            ResetFileDialog(
                onDismissRequest = imageScannerViewModel::hideResetDialog,
                onClear = imageScannerViewModel::resetText
            )
        }
    }
}


@Preview
@Composable
fun BridgeScreenPreview() {
    ImageScannerScreen(
        ImageScannerViewModel(path = "", toastHelper = ToastHelper(LocalContext.current)) {},
        FolderUtilityViewModel(ToastHelper(LocalContext.current), ActivityStarterHelper(LocalContext.current))
    )
}
