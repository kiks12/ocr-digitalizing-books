package com.example.ocr_digital.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun FileFolderMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onRenameClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoveClick: () -> Unit,
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
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
                Text(text = "Move")
            },
            onClick = onMoveClick,
            trailingIcon = { Icon(Icons.Default.KeyboardArrowRight, "Move") }
        )
    }
}
