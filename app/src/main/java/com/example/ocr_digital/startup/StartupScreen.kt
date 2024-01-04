package com.example.ocr_digital.startup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

@Composable
fun StartupScreen(startupViewModel: StartupViewModel) {
    val state = startupViewModel.state

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            Column(
                modifier = Modifier.padding(30.dp)
            ){
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Account Registration",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Please fill up all the fields to continue",
                )
                Spacer(modifier = Modifier.height(35.dp))
                OutlinedTextField(
                    value = state.firstName,
                    onValueChange = startupViewModel::onFirstNameChange,
                    label = { Text(text = "First Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.lastName,
                    onValueChange = startupViewModel::onLastNameChange,
                    label = { Text(text = "Last Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = state.contactNumber,
                    onValueChange = startupViewModel::onContactNumberChange,
                    label = { Text(text = "Contact Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(30.dp))
                Button(
                    onClick = startupViewModel::register,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Register",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
                FilledTonalButton(
                    onClick = startupViewModel::signOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Text(
                        text = "Sign Out",
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun StartupScreenPreview() {
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    val toastHelper = ToastHelper(LocalContext.current)
    StartupScreen(startupViewModel = StartupViewModel(activityStarterHelper, toastHelper))
}