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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun File(
    path: String,
    admin: Boolean,
    filename: String,
    onDeleteClick: () -> Unit,
    onRenameClick: () -> Unit,
    onCopyClick: () -> Unit,
    onTranslateClick: () -> Unit,
    onDownloadClick: () -> Unit,
    onPrintClick: () -> Unit,
    onDetailsClick: () -> Unit,
    onClick: () -> Unit,
    authenticated: Boolean,
) {
    val extension = filename.split(".")[filename.split(".").size - 1]
    var menuExpanded by remember { mutableStateOf(false) }

    val colorMap = mapOf(
        "png" to Color(252, 161, 3),
        "jpeg" to Color(252, 161, 3),
        "jpg" to Color(252, 161, 3),
        "pdf" to Color.Red,
        "docx" to Color(33, 126, 255)
    )

    ListItem(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        leadingContent = {
                if (extension == "png") {
                    Text(
                        text = "PNG",
                        color = colorMap[extension]!!,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (extension == "jpeg" || extension == "jpg") {
                    Text(
                        text = "JPEG",
                        color = colorMap[extension]!!,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (extension == "pdf") {
                    Text(
                        text = "PDF",
                        color = colorMap[extension]!!,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                if (extension == "docx") {
                    Text(
                        text = "DOCX",
                        color = colorMap[extension]!!,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
        },
        headlineContent = { Text(text = filename) },
        trailingContent = {
            Box{
                Icon(
                    Icons.Default.MoreVert,
                    "More",
                    modifier = Modifier.clickable {
                        menuExpanded = true
                    }
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
                        onCopyClick()
                    },
                    onTranslateClick = {
                        menuExpanded = false
                        onTranslateClick()
                    },
                    onDownloadClick = {
                        menuExpanded = false
                        onDownloadClick()
                    },
                    onPrintClick = {
                        menuExpanded = false
                        onPrintClick()
                    },
                    onDetailsClick = {
                        menuExpanded = false
                        onDetailsClick()
                    },
                    forFile = true,
                    path = path,
                    authenticated = authenticated,
                    admin = admin
                )
            }
        }
    )
}

@Preview
@Composable
fun FilePreview() {
    Column {
        File(
            path = "",
            filename = "asdfsadf.docx",
            onDeleteClick = {},
            onRenameClick = {},
            onCopyClick = {},
            onDownloadClick = {},
            onPrintClick = {},
            onClick = {},
            onTranslateClick = {},
            onDetailsClick = {},
            authenticated = false,
            admin = false
        )
    }
}