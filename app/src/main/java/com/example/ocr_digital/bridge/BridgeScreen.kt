package com.example.ocr_digital.bridge

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.camera.CameraActivity
import com.example.ocr_digital.components.dialogs.ResetFileDialog
import com.example.ocr_digital.components.dialogs.SaveFileDialog
import com.example.ocr_digital.components.plain_text.PlainTextEditor
import com.example.ocr_digital.gallery.GalleryActivity
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BridgeScreen(bridgeViewModel: BridgeViewModel) {
    val state = bridgeViewModel.state

    val localContext = LocalContext.current
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val text = result.data?.getStringExtra("ActivityResult") ?: ""
        if (state.text == "") {
            bridgeViewModel.onTextChange(state.text + text)
        } else {
            bridgeViewModel.onTextChange(state.text + "\n" + text)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val text = result.data?.getStringExtra("ActivityResult") ?: ""
        if (state.text == "") {
            bridgeViewModel.onTextChange(state.text + text)
        } else {
            bridgeViewModel.onTextChange(state.text + "\n" + text)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = bridgeViewModel::finish) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                },
                title = { Text(text = "Image Scanner") },
                actions = {
                    IconButton(onClick = bridgeViewModel::showResetDialog) {
                        Icon(Icons.Default.Refresh, "Reset")
                    }
                    IconButton(onClick = bridgeViewModel::showSaveDialog) {
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
                    Icon(Icons.Default.Add, "Camera")
                }
                Spacer(modifier = Modifier.height(15.dp))
                FloatingActionButton(
                    onClick = { galleryLauncher.launch(Intent(localContext, GalleryActivity::class.java)) },
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.AccountBox, "Upload")
                }
            }
        }
    )
    { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(15.dp)
        ){
            item {
                PlainTextEditor(text = state.text, onTextChanged = bridgeViewModel::onTextChange)
            }
        }

        if (state.showSaveDialog) {
            SaveFileDialog(
                filename = state.filename,
                onFileNameChange = bridgeViewModel::onFileNameChange,
                onFileTypeChange = bridgeViewModel::onFileTypeChange,
                onDismissRequest = bridgeViewModel::hideSaveDialog,
                onSave = { bridgeViewModel.saveFile(localContext, state.text, state.filename, state.filetype) }
            )
        }

        if (state.showResetDialog) {
            ResetFileDialog(
                onDismissRequest = bridgeViewModel::hideResetDialog,
                onClear = bridgeViewModel::resetText
            )
        }
    }
}


@Preview
@Composable
fun BridgeScreenPreview() {
    BridgeScreen(BridgeViewModel(path = "", toastHelper = ToastHelper(LocalContext.current)) {})
}
