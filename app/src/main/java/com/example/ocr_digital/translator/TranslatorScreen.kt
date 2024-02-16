package com.example.ocr_digital.translator

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.components.plain_text.PlainTextEditor
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslatorScreen(translatorViewModel: TranslatorViewModel) {
    val context = LocalContext.current
    val state = translatorViewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Translator") },
                navigationIcon = {
                    IconButton(onClick = translatorViewModel::finish) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                },
                actions = {
                    FilledTonalButton(
                        onClick = { translatorViewModel.save(context) },
                        modifier = Modifier.padding(start = 0.dp, top = 0.dp, end = 5.dp, bottom = 0.dp)
                    ) {
                        Text(text = "Save")
                    }
                }
            )
        },
        bottomBar = {
            Column(modifier = Modifier.padding(15.dp)) {
                Row {
                    Box(
                        modifier = Modifier.fillMaxWidth(0.45f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text = "Source Language", fontSize = 11.sp)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        translatorViewModel.onSourceDropDownExpandedChange(
                                            true
                                        )
                                    },
                                text = state.sourceDropDownSelectedText,
                                textAlign = TextAlign.Center
                            )
                        }
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
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = "Source to Target")
                    Spacer(modifier = Modifier.width(5.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "Target Language", fontSize = 11.sp)
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        translatorViewModel.onTargetDropDownExpandedChange(
                                            true
                                        )
                                    },
                                text = state.targetDropDownSelectedText,
                                textAlign = TextAlign.Center
                            )
                        }

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
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = translatorViewModel::translate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(text = "Translate")
                }
            }
        }
    ) { innerPadding ->
        if (state.loading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
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
}


@Preview
@Composable
fun TranslatorScreenPreview() {
    val localContext = LocalContext.current
    TranslatorScreen(TranslatorViewModel("" ,"", "", ToastHelper(localContext)){})
}