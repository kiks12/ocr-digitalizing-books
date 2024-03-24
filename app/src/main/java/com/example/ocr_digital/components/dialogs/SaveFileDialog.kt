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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.ocr_digital.components.Folder
import com.example.ocr_digital.file_saver.FileType
import com.example.ocr_digital.path.PathUtilities
import com.google.firebase.storage.StorageReference

@Composable
fun SaveFileDialog(
    filename: String,
    onFileNameChange: (newStr: String) -> Unit,
    onDismissRequest: () -> Unit,
    onFileTypeChange: (type: FileType) -> Unit,
    parentFolder: String,
    selectedFolder: String,
    setSelectedFolder : (path: String) -> Unit,
    folders: List<StorageReference>,
    onSave: () -> Unit,
    showCreateFolderDialog: () -> Unit,
) {
    var showFolderMenu by remember { mutableStateOf(false) }
    var showFileNameCard by remember { mutableStateOf(false) }
    var filetype by remember { mutableStateOf(FileType.DOCX) }

    fun showFileNameCardAndFileType(type: FileType) {
        showFileNameCard = true
        onFileTypeChange(type)
        filetype = type
    }

    val colorMap = mapOf(
        "png" to Color(252, 161, 3),
        "jpeg" to Color(252, 161, 3),
        "pdf" to Color.Red,
        "docx" to Color(33, 126, 255)
    )

    Dialog(onDismissRequest = onDismissRequest) {
        if (!showFileNameCard) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(text = "Save as")
                    Spacer(modifier = Modifier.height(15.dp))
                    TextButton(
                        onClick = { showFileNameCardAndFileType(FileType.DOCX) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save as DOCX")
                    }
                    TextButton(
                        onClick = { showFileNameCardAndFileType(FileType.PDF) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save as PDF")
                    }
                    TextButton(
                        onClick = { showFileNameCardAndFileType(FileType.PNG) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save as PNG")
                    }
                    TextButton(
                        onClick = { showFileNameCardAndFileType(FileType.JPEG) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Save as JPEG")
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    TextButton(
                        onClick = onDismissRequest,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "Cancel")
                    }
                }
            }
        }

        if (showFileNameCard) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp)
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
                        Text(text = "Save as")
                        Text(
                            text = filetype.toString(),
                            color = colorMap[filetype.toString().lowercase()]!!,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                    Text(text = "Select Folder:")
                    Spacer(modifier = Modifier.height(5.dp))
                    Box {
                        Folder(
                            directoryName = if (parentFolder == selectedFolder) "/" else PathUtilities.getLastSegment(selectedFolder),
                            onRenameClick = { },
                            onDeleteClick = { },
                            onMoveClick = { },
                            onFolderClick = { showFolderMenu = true },
                            showVerticalDots = false)
                        DropdownMenu(expanded = showFolderMenu, onDismissRequest = { showFolderMenu = false }) {
                            if (parentFolder != selectedFolder) {
                                DropdownMenuItem(text = { Text("Go Back") }, onClick = {
                                    setSelectedFolder(PathUtilities.removeLastSegment(selectedFolder))
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
                    Spacer(modifier = Modifier.height(15.dp))

                    OutlinedTextField(
                        value = filename,
                        onValueChange = onFileNameChange,
                        label = { Text("Filename") }
                    )
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
                            Text(text = "Save")
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun SaveFileDialogPreview() {
    SaveFileDialog(
        filename = "",
        onFileNameChange = {},
        onFileTypeChange = {},
        onDismissRequest = {},
        onSave = {},
        folders = listOf(),
        setSelectedFolder = {},
        parentFolder = "",
        selectedFolder = "",
        showCreateFolderDialog = {}
    )
}