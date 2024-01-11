package com.example.ocr_digital.passwords.change_password

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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ocr_digital.helpers.ToastHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordScreen(changePasswordViewModel: ChangePasswordViewModel) {
    val state = changePasswordViewModel.state
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Change Password") },
                navigationIcon = {
                    IconButton(onClick = changePasswordViewModel::finish) {
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
                value = state.currentPassword,
                onValueChange = changePasswordViewModel::onCurrentPasswordChange,
                visualTransformation = if (state.showCurrentPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("Current Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically){
                Checkbox(checked = state.showCurrentPassword, onCheckedChange = changePasswordViewModel::onCurrentCheckboxChange)
                Text("Show Current Password")
            }
            OutlinedTextField(
                value = state.newPassword,
                onValueChange = changePasswordViewModel::onNewPasswordChange,
                visualTransformation = if (state.showNewPassword) VisualTransformation.None else PasswordVisualTransformation(),
                label = { Text("New Password") },
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically){
                Checkbox(checked = state.showNewPassword, onCheckedChange = changePasswordViewModel::onNewCheckboxChange)
                Text("Show New Password")
            }

            Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                Button(onClick = changePasswordViewModel::changePassword) {
                    Text(text = "Change Password")
                }
            }
        }
    }
}


@Preview
@Composable
fun ChangePasswordScreenPreview() {
    val toastHelper = ToastHelper(LocalContext.current)
    ChangePasswordScreen(ChangePasswordViewModel(toastHelper) {})
}