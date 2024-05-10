package com.example.ocr_digital.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import compose.icons.FeatherIcons
import compose.icons.feathericons.Copy
import compose.icons.feathericons.Download
import compose.icons.feathericons.FileText
import compose.icons.feathericons.Info
import compose.icons.feathericons.Printer

@Composable
fun FileFolderMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onCopyClick: () -> Unit,
    onTranslateClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
    onPrintClick: () -> Unit = {},
    onDetailsClick: () -> Unit = {},
    authenticated: Boolean,
    forFile: Boolean = false,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        if (authenticated) {
            DropdownMenuItem(
                text = {
                    Text(text = "Rename")
                },
                onClick = onRenameClick,
                trailingIcon = { Icon(Icons.Default.Edit, "Rename") }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Delete")
                },
                onClick = onDeleteClick,
                trailingIcon = { Icon(Icons.Default.Delete, "Delete") }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "Copy")
                },
                onClick = onCopyClick,
                trailingIcon = { Icon(FeatherIcons.Copy, "Copy") }
            )
        }
        if (forFile) {
            if (authenticated) {
                DropdownMenuItem(
                    text = { Text(text = "Translate") },
                    onClick = onTranslateClick,
                    trailingIcon = { Icon(FeatherIcons.FileText, "Translate") }
                )
            }
            DropdownMenuItem(
                text = { Text(text = "Download") },
                onClick = onDownloadClick,
                trailingIcon = { Icon(FeatherIcons.Download, "Download") }
            )
            DropdownMenuItem(
                text = { Text("Print") },
                onClick = onPrintClick,
                trailingIcon = { Icon(FeatherIcons.Printer, "Print") }
            )
            DropdownMenuItem(
                text = { Text("Details") },
                onClick = onDetailsClick ,
                trailingIcon = { Icon(FeatherIcons.Info, "Info") }
            )
        }
    }
}

@Preview
@Composable
fun FileFolderMenuPreview() {
    FileFolderMenu(
        expanded = true,
        onDismissRequest = {},
        onRenameClick = {},
        onDeleteClick = {},
        onCopyClick = {},
        forFile = true,
        onDownloadClick = {},
        authenticated = false
    )
}