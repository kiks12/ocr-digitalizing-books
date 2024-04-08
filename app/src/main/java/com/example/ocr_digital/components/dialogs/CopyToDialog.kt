package com.example.ocr_digital.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.path.PathUtilities
import com.google.firebase.storage.StorageReference

@Composable
fun CopyToDialog(
    folders : List<StorageReference>,
    parentFolder : String,
    selectedFolder : String,
    setSelectedFolder : (path: String) -> Unit,
    showCreateFolderDialog : () -> Unit,
    onDismissRequest : () -> Unit,
    onSave : () -> Unit,
) {
    var showFolderMenu by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(5.dp),
            shape = RoundedCornerShape(16.dp),
        ){
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Copy To")
                }
                Spacer(modifier = Modifier.height(25.dp))
                Text(text = "Select Folder:")
                Spacer(modifier = Modifier.height(5.dp))
                Box {
                    Folder(
                        directoryName = if (parentFolder == selectedFolder) "/" else if (selectedFolder == "/XvltUpvBv8J6lhL8kNCB") "Public Library" else PathUtilities.getLastSegment(selectedFolder),
                        onRenameClick = { },
                        onDeleteClick = { },
                        onMoveClick = { },
                        onFolderClick = { showFolderMenu = true },
                        authenticated = false,
                        showVerticalDots = false
                    )
                    DropdownMenu(expanded = showFolderMenu, onDismissRequest = { showFolderMenu = false }) {
                        if (parentFolder != selectedFolder) {
                            DropdownMenuItem(text = { Text("Go Back") }, onClick = {
                                if (selectedFolder == "/XvltUpvBv8J6lhL8kNCB") {
                                    setSelectedFolder(parentFolder)
                                } else {
                                    setSelectedFolder(PathUtilities.removeLastSegment(selectedFolder))
                                }
                                showFolderMenu = false
                            })
                        }
                        if (parentFolder == selectedFolder) {
                            DropdownMenuItem(text = { Text("Public Library") }, onClick = {
                                setSelectedFolder("/XvltUpvBv8J6lhL8kNCB")
                                showFolderMenu = false
                            })
                        }
                        folders.forEach {
                            DropdownMenuItem(text = { Text(it.name) }, onClick = {
                                setSelectedFolder(it.path)
                                showFolderMenu = false
                            })
                        }
                    }
                }
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    TextButton(onClick = showCreateFolderDialog) {
                        Text("Create Folder")
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(15.dp))
                    Button(onClick = onSave) {
                        Text(text = "Copy")
                    }
                }
            }
        }
    }
}