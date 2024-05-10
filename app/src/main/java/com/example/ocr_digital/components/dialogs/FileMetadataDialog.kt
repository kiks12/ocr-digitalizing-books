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
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ocr_digital.file_saver.FileMetadata
import com.google.firebase.Timestamp

@Composable
fun FileMetadataDialog(metadata: FileMetadata, onDismissRequest: () -> Unit, update: (metadata: FileMetadata) -> Unit) {
    var showEditInfo by remember { mutableStateOf(false) }
    var metadataState by remember {
        mutableStateOf(
            FileMetadata(
                title = metadata.title,
                author = metadata.author,
                genre =  metadata.genre,
                path = metadata.path,
                createdAt = metadata.createdAt,
                publishedYear = metadata.publishedYear
            )
        )
    }

    Dialog(onDismissRequest = onDismissRequest) {
        if (!showEditInfo) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(470.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(text = "Book Information", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Title: ", fontSize = 10.sp)
                        Text(text = metadata.title)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Author: ", fontSize = 10.sp)
                        Text(text = metadata.author)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Genre: ", fontSize = 10.sp)
                        Text(text = metadata.genre)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "Publish Year: ", fontSize = 10.sp)
                        Text(text = metadata.publishedYear)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "File Created At: ", fontSize = 10.sp)
                        Text(text = metadata.createdAt.toDate().toLocaleString())
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        Text(text = "File Path: ", fontSize = 10.sp)
                        Text(text = metadata.path, fontSize = 10.sp)
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        FilledTonalButton(onClick = { showEditInfo = true }) {
                            Text(text = "Edit Info")
                        }
                        Button(onClick = onDismissRequest) {
                            Text(text = "Close")
                        }
                    }
                }
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(470.dp)
                    .padding(5.dp),
                shape = RoundedCornerShape(16.dp),
            ){
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center
                ){
                    Text(text = "Edit Book Information", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(30.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        OutlinedTextField(value = metadataState.title, onValueChange = { metadataState = metadataState.copy(title=it) }, label = { Text("Title") })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        OutlinedTextField(value = metadataState.author, onValueChange = { metadataState = metadataState.copy(author=it) }, label = { Text("Author") })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        OutlinedTextField(value = metadataState.genre, onValueChange = { metadataState = metadataState.copy(genre=it) }, label = { Text("Genre") })
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(modifier = Modifier.fillMaxWidth()){
                        OutlinedTextField(value = metadataState.publishedYear, onValueChange = { metadataState = metadataState.copy(publishedYear=it) }, label = { Text("Publish Year") })
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ){
                        TextButton(onClick = { showEditInfo = false }) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Button(onClick = { update(metadataState.copy()) }) {
                            Text(text = "Update")
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun FileMetadataDialogPreview() {
    FileMetadataDialog(metadata = FileMetadata(
        title = "SAMPLE TITLE",
        author = "Sample Author",
        genre = "HORROR",
        publishedYear = "2024",
        createdAt = Timestamp.now(),
        path = ""
    ), {}, {})
}
