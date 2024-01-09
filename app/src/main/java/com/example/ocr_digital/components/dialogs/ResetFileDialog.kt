package com.example.ocr_digital.components.dialogs

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ResetFileDialog(onDismissRequest: () -> Unit, onClear: () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest) {
        Card (
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ){
            Column(
                modifier = Modifier.padding(20.dp).fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Reset Text",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = "Are you sure you want to reset the text?")
                Spacer(modifier = Modifier.height(15.dp))
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    TextButton(onClick = onDismissRequest) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Button(onClick = onClear) {
                        Text(text = "Reset")
                    }
                }

            }
        }
    }
}


@Preview
@Composable
fun ResetFileDialogPreview() {
    ResetFileDialog(onDismissRequest = {}) {}
}