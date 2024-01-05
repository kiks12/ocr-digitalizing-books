package com.example.ocr_digital.bridge

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
import com.example.ocr_digital.components.plain_text.PlainTextEditor
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BridgeScreen(bridgeViewModel: BridgeViewModel) {
    val state = bridgeViewModel.state

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
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Refresh, "Reset")
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        },
        floatingActionButton = {
            Column {
                FloatingActionButton(
                    onClick = bridgeViewModel::useCamera,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, "Camera")
                }
                Spacer(modifier = Modifier.height(15.dp))
                FloatingActionButton(
                    onClick = bridgeViewModel::uploadImages,
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
    }
}


@Preview
@Composable
fun BridgeScreenPreview() {
    val toastHelper = ToastHelper(LocalContext.current)
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    BridgeScreen(BridgeViewModel(toastHelper, activityStarterHelper) {})
}
