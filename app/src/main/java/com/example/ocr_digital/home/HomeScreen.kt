package com.example.ocr_digital.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ocr_digital.components.File
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {
    val sheetState = rememberModalBottomSheetState()
    val state = homeViewModel.state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = homeViewModel::showBottomSheet) {
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
                Folder(
                    directoryName = folder.name,
                    onDeleteClick = { homeViewModel.showDeleteFileOrFolderDialog(folder.path) },
                    onRenameClick = {},
                    onMoveClick = {}
                )
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
            item {
                Text(text = "Files")
            }
            items(state.files) {file ->
                File(
                    filename = file.name,
                    onDeleteClick = { homeViewModel.showDeleteFileOrFolderDialog(file.path) }
                )
            }
        }

        if (state.showBottomSheet) {
            CreateActionsBottomSheet(
                sheetState = sheetState,
                onDismissRequest = homeViewModel::hideBottomSheet,
                useCamera = homeViewModel::useCamera,
                uploadImages = homeViewModel::uploadImages,
                createFolder = homeViewModel::showCreateFolderDialog
            )
        }

        if (state.showCreateFolderDialog) {
            CreateFolderDialog(
                folderName = state.folderName,
                onFolderNameChange = homeViewModel::onFolderNameChange,
                onDismissRequest = homeViewModel::hideCreateFolderDialog,
                onCreateClick = homeViewModel::createFolder
            )
        }

        if (state.showDeleteFileOrFolderDialog) {
            DeleteFileOrFolderDialog(
                onDismissRequest = homeViewModel::hideDeleteFileOrFolderDialog,
                onDeleteClick = homeViewModel::deleteFileOrFolder
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

@Composable
fun DeleteFileOrFolderDialog(
    onDismissRequest: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Text(
                    text = "Delete File/Folder",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Are you sure you want to delete this?")
                Spacer(modifier = Modifier.height(35.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = onDeleteClick) {
                        Text(text = "Delete")
                    }
                }

            }
        }
    }
}

@Composable
fun CreateFolderDialog(
    folderName: String,
    onFolderNameChange: (newString: String) -> Unit,
    onDismissRequest: () -> Unit,
    onCreateClick: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ){
                Text(
                    text = "Create Folder",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(15.dp))
                OutlinedTextField(
                    value = folderName,
                    onValueChange = onFolderNameChange,
                    label = { Text("Folder Name")},
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(35.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = onCreateClick) {
                        Text(text = "Create")
                    }
                }
            }
        }
    }
}



@Preview
@Composable
fun DeleteFileOrFolderDialogPreview() {
    DeleteFileOrFolderDialog(
        onDismissRequest = {},
        onDeleteClick = {}
    )
}

@Preview
@Composable
fun CreateFolderDialogPreview() {
    CreateFolderDialog(
        folderName = "",
        onFolderNameChange = {},
        onDismissRequest = {},
        onCreateClick = {}
    )
}

@Preview
@Composable
fun HomeScreenPreview() {
    val toastHelper = ToastHelper(LocalContext.current)
    HomeScreen(homeViewModel = HomeViewModel(toastHelper))
}