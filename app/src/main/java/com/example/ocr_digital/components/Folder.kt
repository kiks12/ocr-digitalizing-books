package com.example.ocr_digital.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun Folder(
    directoryName: String,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveClick: () -> Unit,
    onFolderClick: () -> Unit
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    ListItem(
        modifier = Modifier.fillMaxWidth().clickable { onFolderClick() },
        leadingContent = {},
        headlineContent = { Text(text = directoryName) },
        trailingContent = {
            Box{
                IconButton(onClick = { menuExpanded = true}) {
                    Icon(Icons.Default.MoreVert, "More")
                }
                FileFolderMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false },
                    onRenameClick = onRenameClick,
                    onDeleteClick = onDeleteClick,
                    onMoveClick = onMoveClick
                )
            }
        }
    )
}



@Preview(backgroundColor = 1)
@Composable
fun FolderPreview() {
    Column {
        Folder(
            directoryName = "AEC",
            onRenameClick = {},
            onMoveClick = {},
            onDeleteClick = {},
            onFolderClick = {}
        )
        Folder(
            directoryName = "TRY",
            onRenameClick = {},
            onMoveClick = {},
            onDeleteClick = {},
            onFolderClick = {}
        )
        Folder(
            directoryName = "TRY 2",
            onRenameClick = {},
            onMoveClick = {},
            onDeleteClick = {},
            onFolderClick = {}
        )
    }
}