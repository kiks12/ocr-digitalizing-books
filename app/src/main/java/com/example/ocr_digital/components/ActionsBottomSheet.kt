package com.example.ocr_digital.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionsBottomSheet(
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
fun ActionsBottomSheetPreview() {
    Column(
        modifier = Modifier.padding(30.dp)
    ){
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Camera",
                modifier = Modifier.padding(10.dp)
            )
        }
        FilledTonalButton(
            onClick = {},
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
            onClick = {},
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Folder",
                modifier = Modifier.padding(10.dp)
            )
        }
    }
}