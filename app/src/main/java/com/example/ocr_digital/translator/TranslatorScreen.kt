package com.example.ocr_digital.translator

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
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
            Column(
                modifier = Modifier.padding(10.dp)
            ){
                Column {
                    Text(text = "Source Language")
                    ExposedDropdownMenuBox(
                        expanded = state.sourceDropDownExpanded,
                        onExpandedChange = translatorViewModel::onSourceDropDownExpandedChange
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.sourceDropDownSelectedText,
                            onValueChange = {},
                            readOnly = true
                        )

                        ExposedDropdownMenu(expanded = state.sourceDropDownExpanded, onDismissRequest = {
                            translatorViewModel.onSourceDropDownExpandedChange(false)
                        }) {
                            DropdownMenuItem(
                                text = { Text(text = "Tagalog") },
                                onClick = { translatorViewModel.onSourceDropDownSelectedTextChange("Filipino") }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "English") },
                                onClick = { translatorViewModel.onSourceDropDownSelectedTextChange("English") }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(15.dp))
                Column {
                    Text(text = "Target Language")
                    ExposedDropdownMenuBox(
                        expanded = state.targetDropDownExpanded,
                        onExpandedChange = translatorViewModel::onTargetDropDownExpandedChange
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = state.targetDropDownSelectedText,
                            onValueChange = {},
                            readOnly = true
                        )

                        ExposedDropdownMenu(expanded = state.targetDropDownExpanded, onDismissRequest = {
                            translatorViewModel.onTargetDropDownExpandedChange(false)
                        }) {
                            DropdownMenuItem(
                                text = { Text(text = "Tagalog") },
                                onClick = { translatorViewModel.onTargetDropDownSelectedTextChange("Filipino") }
                            )
                            DropdownMenuItem(
                                text = { Text(text = "English") },
                                onClick = { translatorViewModel.onTargetDropDownSelectedTextChange("English") }
                            )
                        }
                    }
                }
                Button(onClick = translatorViewModel::translate) {
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