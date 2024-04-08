package com.example.ocr_digital.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import compose.icons.FeatherIcons
import compose.icons.feathericons.Folder

@Composable
fun Folder(
    directoryName: String,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveClick: () -> Unit,
    onFolderClick: () -> Unit,
    authenticated: Boolean,
    showVerticalDots: Boolean = true,
) {
    var menuExpanded by remember {
        mutableStateOf(false)
    }

    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFolderClick() },
        leadingContent = {
            Icon(FeatherIcons.Folder, directoryName)
        },
        headlineContent = { Text(text = directoryName) },
        trailingContent = {
            if (showVerticalDots) {
                Box{
                    Icon(
                        Icons.Default.MoreVert,
                        "More",
                        modifier = Modifier.clickable { menuExpanded = true }
                    )
                    FileFolderMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false },
                        onRenameClick = {
                            menuExpanded = false
                            onRenameClick()
                        },
                        onDeleteClick = {
                            menuExpanded = false
                            onDeleteClick()
                        },
                        onCopyClick = {
                            menuExpanded = false
                            onMoveClick()
                        },
                        authenticated = authenticated
                    )
                }
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
            onFolderClick = {},
            authenticated = false
        )
        Folder(
            directoryName = "TRY",
            onRenameClick = {},
            onMoveClick = {},
            onDeleteClick = {},
            onFolderClick = {},
            authenticated = false
        )
        Folder(
            directoryName = "TRY 2",
            onRenameClick = {},
            onMoveClick = {},
            onDeleteClick = {},
            onFolderClick = {},
            authenticated = false
        )
    }
}