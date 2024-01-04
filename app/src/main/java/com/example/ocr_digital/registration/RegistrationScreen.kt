package com.example.ocr_digital.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper

@Composable
fun RegistrationScreen(registrationViewModel: RegistrationViewModel) {
    val state = registrationViewModel.state

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxSize()
            ) {
                Column {
                    Column {
                        Text(
                            text = "OCR: Digital Books",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = "Register new account")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Column {
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = registrationViewModel::onEmailChange,
                            label = { Text(text = "Email")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.password,
                            onValueChange = registrationViewModel::onPasswordChange,
                            label = { Text(text = "Password")},
                            visualTransformation = if (state.seePassword) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Checkbox(
                                checked = state.seePassword,
                                onCheckedChange = registrationViewModel::onSeePasswordChange,
                            )
                            Text(text = "Show Password")
                        }
                        OutlinedTextField(
                            value = state.firstName,
                            onValueChange = registrationViewModel::onFirstNameChange,
                            label = { Text(text = "First Name")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.lastName,
                            onValueChange = registrationViewModel::onLastNameChange,
                            label = { Text(text = "last Name")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.contactNumber,
                            onValueChange = registrationViewModel::onContactNumberChange,
                            label = { Text(text = "Contact Number")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = registrationViewModel::register,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Register",
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = "Already have an Account?")
                    TextButton(
                        onClick = registrationViewModel::startLoginActivity,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Login here",
                            modifier = Modifier.padding(vertical = 10.dp)
                        )
                    }
                }
            }
        }
    }
}


@Preview
@Composable
private fun LoginScreenPreview() {
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    val toastHelper = ToastHelper(LocalContext.current)
    RegistrationScreen(
        registrationViewModel = RegistrationViewModel(
            activityStarterHelper = activityStarterHelper,
            toastHelper = toastHelper
        )
    )
}