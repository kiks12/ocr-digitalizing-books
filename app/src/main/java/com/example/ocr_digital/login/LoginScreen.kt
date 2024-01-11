package com.example.ocr_digital.login

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.FilledTonalButton
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
fun LoginScreen(loginViewModel: LoginViewModel) {
    val state = loginViewModel.state

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
                        Text(text = "Login to Continue")
                    }
                    Spacer(modifier = Modifier.height(30.dp))
                    Column {
                        OutlinedTextField(
                            value = state.email,
                            onValueChange = loginViewModel::onEmailChange,
                            label = { Text(text = "Email")},
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = state.password,
                            onValueChange = loginViewModel::onPasswordChange,
                            label = { Text(text = "Password")},
                            visualTransformation = if (state.seePassword) VisualTransformation.None else PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                Checkbox(
                                    checked = state.seePassword,
                                    onCheckedChange = loginViewModel::onSeePasswordChange,
                                )
                                Text(text = "Show Password")
                            }
                            Text(
                                text = "Forgot Password",
                                fontSize = 15.sp,
                                modifier = Modifier.clickable { loginViewModel.startForgotPasswordActivity() }
                            )
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Button(
                            onClick = loginViewModel::loginWithEmailAndPassword,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Login",
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                        FilledTonalButton(
                            onClick = loginViewModel::continueWithGoogleHelper,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                        ) {
                            Text(
                                text = "Continue with Google",
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(text = "Don't Have an Account?")
                    TextButton(
                        onClick = loginViewModel::startRegistrationActivity,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Register here",
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
    LoginScreen(
        loginViewModel = LoginViewModel(
            activityStarterHelper = activityStarterHelper,
            toastHelper = toastHelper,
            continueWithGoogle = {  }
        )
    )
}