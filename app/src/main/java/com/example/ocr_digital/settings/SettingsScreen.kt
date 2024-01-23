package com.example.ocr_digital.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
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
import compose.icons.FeatherIcons
import compose.icons.feathericons.Info
import compose.icons.feathericons.Lock
import compose.icons.feathericons.LogOut

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
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