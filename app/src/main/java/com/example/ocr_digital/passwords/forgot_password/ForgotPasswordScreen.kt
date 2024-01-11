package com.example.ocr_digital.passwords.forgot_password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(forgotPasswordViewModel: ForgotPasswordViewModel) {
    val email = forgotPasswordViewModel.state

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password") },
                navigationIcon = {
                    IconButton(onClick = forgotPasswordViewModel::finish) {
                        Icon(Icons.Default.ArrowBack, "Go Back")
                    }
                }
            )
        }
    ){
        Column(
            modifier = Modifier
                .padding(it)
                .padding(15.dp)
        ){
            Spacer(Modifier.height(15.dp))
            Text(text = "Fill up the fields to continue")
            Spacer(Modifier.height(20.dp))

            OutlinedTextField(
                value = email,
                onValueChange = forgotPasswordViewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(15.dp))
            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                Button(onClick = forgotPasswordViewModel::sendResetEmail) {
                    Text(text = "Send Email")
                }
            }
        }
    }
}