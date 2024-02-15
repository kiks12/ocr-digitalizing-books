package com.example.ocr_digital.translator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.components.plain_text.PlainTextEditor
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(translatorViewModel: TranslatorViewModel, finishCallback: () -> Unit) {
    val state = translatorViewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Translator") },
                navigationIcon = {
                    IconButton(onClick = finishCallback) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(10.dp)) {
                Row {
                    Box(modifier = Modifier.fillMaxWidth(0.48f)) {
                        OutlinedTextField(
                            modifier = Modifier.clickable { translatorViewModel.onSourceDropDownExpandedChange(true) },
                            value = state.sourceDropDownSelectedText,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Source Language")}
                        )
                        DropdownMenu(expanded = state.sourceDropDownExpanded, onDismissRequest = { translatorViewModel.onSourceDropDownExpandedChange(false) }) {
                            DropdownMenuItem(
                                text = { Text(text = "Tagalog") },
                                onClick = {
                                    translatorViewModel.onSourceDropDownSelectedTextChange(
                                        "Filipino"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "English") },
                                onClick = {
                                    translatorViewModel.onSourceDropDownSelectedTextChange(
                                        "English"
                                    )
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            modifier = Modifier.clickable { translatorViewModel.onSourceDropDownExpandedChange(true) },
                            value = state.targetDropDownSelectedText,
                            onValueChange = {},
                            label = { Text(text = "Target Language") },
                            readOnly = true
                        )

                        DropdownMenu(expanded = state.targetDropDownExpanded, onDismissRequest = { translatorViewModel.onTargetDropDownExpandedChange(false) }) {
                            DropdownMenuItem(
                                text = { Text(text = "Tagalog") },
                                onClick = {
                                    translatorViewModel.onTargetDropDownSelectedTextChange(
                                        "Filipino"
                                    )
                                }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "English") },
                                onClick = {
                                    translatorViewModel.onTargetDropDownSelectedTextChange(
                                        "English"
                                    )
                                }
                            )
                        }
                    }
                }
                Button(
                    onClick = translatorViewModel::translate,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Translate")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(15.dp)
        ) {
            item {
                PlainTextEditor(text = state.text, onTextChanged = translatorViewModel::onTextChange)
            }
        }
    }
}


@Preview
@Composable
fun TranslatorScreenPreview() {
    val localContext = LocalContext.current
    TranslatorScreen(TranslatorViewModel("", ToastHelper(localContext))) {}
}