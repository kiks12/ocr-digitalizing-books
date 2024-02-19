package com.example.ocr_digital.onboarding.walkthrough

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.onboarding.walkthrough.create_folder.CreateFolderWalkthroughScreen
import com.example.ocr_digital.onboarding.walkthrough.extract_text.ExtractTextWalkthroughScreen
import com.example.ocr_digital.onboarding.walkthrough.translate_files.TranslateFilesWalkthroughScreen
import com.example.ocr_digital.ui.theme.OcrDigitalTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WalkthroughScreen(walkthroughViewModel: WalkthroughViewModel) {
    val automatic = walkthroughViewModel.automaticState
    val loading = walkthroughViewModel.loadingState
    var step by remember {
        mutableIntStateOf(0)
    }

    if (loading) {
        Scaffold { innerPadding ->
            Box(modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    } else {
        if (automatic) {
            if (step == 0) {
                CreateFolderWalkthroughScreen {
                    step += 1
                }
            }
            if (step == 1) {
                ExtractTextWalkthroughScreen {
                    step += 1
                }
            }
            if (step == 2) {
                TranslateFilesWalkthroughScreen {
                    walkthroughViewModel.finishAutomatic()
                }
            }
        } else {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("How to use?") },
                        navigationIcon = {
                            IconButton(onClick = { walkthroughViewModel.finish() }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Go Back")
                            }
                            }
                    )
                }
            ) { innerPadding ->
                LazyColumn(
                    modifier = Modifier.padding(innerPadding)
                ){
                    item {
                        ListItem(
                            modifier = Modifier.clickable { walkthroughViewModel.startCreateFolderWalkthroughActivity() },
                            headlineContent = { Text("Create a folder") },
                            trailingContent = { Icon(Icons.Default.KeyboardArrowRight, "go") }
                        )
                    }
                    item {
                        ListItem(
                            modifier = Modifier.clickable { walkthroughViewModel.startExtractTextWalkthroughActivity() },
                            headlineContent = { Text("Extract text from image") },
                            trailingContent = { Icon(Icons.Default.KeyboardArrowRight, "go") }
                        )
                    }
                    item {
                        ListItem(
                            modifier = Modifier.clickable { walkthroughViewModel.startTranslateFileWalkthroughActivity() },
                            headlineContent = { Text("Translate file to another language") },
                            trailingContent = { Icon(Icons.Default.KeyboardArrowRight, "go") }
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun WalkthroughScreenPreview() {
    OcrDigitalTheme {
        WalkthroughScreen(WalkthroughViewModel(false, ActivityStarterHelper(LocalContext.current)) {})
    }
}