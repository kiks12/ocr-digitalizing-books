package com.example.ocr_digital.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val state = homeViewModel.state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        },
        topBar = {
            SearchBar(
                query = "",
                onQueryChange = {},
                onSearch = {},
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(25.dp)
                .fillMaxWidth()
        ) {
            item {
                Text(text = "Folders")
            }
            items(state.folders) {folder ->
                Folder(directoryName = folder)
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(text = "Files")
            }
            items(state.files) {file ->
                File(filename = file)
            }
        }

        if (showBottomSheet) {
            CreateActionsBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false },
                useCamera = { /*TODO*/ },
                uploadImages = { /*TODO*/ },
                createFolder = {}
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateActionsBottomSheet(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    useCamera: () -> Unit,
    uploadImages: () -> Unit,
    createFolder: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = Modifier.padding(30.dp)
        ){
            Button(
                onClick = useCamera,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Camera",
                    modifier = Modifier.padding(10.dp)
                )
            }
            FilledTonalButton(
                onClick = uploadImages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Upload Images",
                    modifier = Modifier.padding(10.dp)
                )
            }
            OutlinedButton(
                onClick = createFolder,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Folder",
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}


@Preview
@Composable
fun BottomSheet() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Camera",
                modifier = Modifier.padding(10.dp)
            )
        }
        FilledTonalButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp)
        ) {
            Text(
                text = "Upload Images",
                modifier = Modifier.padding(10.dp)
            )
        }
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Folder",
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(homeViewModel = HomeViewModel())
}