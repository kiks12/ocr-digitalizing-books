package com.example.ocr_digital.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import compose.icons.feathericons.Lock
import compose.icons.feathericons.LogOut

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    val state = settingsViewModel.state

    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            LazyColumn {
                item {
                    Text(
                        text = "Settings",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)
                    )
                }
                item {
                    Column(modifier = Modifier.padding(15.dp)){
                        Text(text = "Profile", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp)
                        ){
                            Text(text = "Email:", fontSize = 12.sp)
                            Text(text = state.email)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Column(
                                modifier = Modifier.fillMaxWidth(0.5f)
                            ){
                                Text(text = "First Name:", fontSize = 12.sp)
                                Text(text = state.userInformation.firstName)
                            }
                            Column(
                                modifier = Modifier.fillMaxWidth(1f)
                            ){
                                Text(text = "Last Name:", fontSize = 12.sp)
                                Text(text = state.userInformation.lastName)
                            }
                        }
                        Column(modifier = Modifier.padding(top=12.dp)){
                            Text(text = "Contact Number:", fontSize = 12.sp)
                            Text(text = state.userInformation.contactNumber)
                        }
                    }
                }
                item { 
                    Spacer(modifier = Modifier.height(20.dp))
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable { settingsViewModel.openWalkthroughActivity() },
                        headlineContent = { Text(text = "How to use?") },
                        trailingContent = { Icon(FeatherIcons.Info, "How to use") }
                    )
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable { settingsViewModel.openChangePasswordActivity() },
                        headlineContent = { Text(text = "Change Password") },
                        trailingContent = { Icon(FeatherIcons.Lock, "Change Password") }
                    )
                }
                item {
                    ListItem(
                        modifier = Modifier.clickable { settingsViewModel.signOut() },
                        headlineContent = { Text(text = "Sign Out") },
                        trailingContent = { Icon(FeatherIcons.LogOut, "Sign Out") }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    val toastHelper = ToastHelper(LocalContext.current)
    val settingsViewModel = SettingsViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)
    SettingsScreen(settingsViewModel = settingsViewModel)
}