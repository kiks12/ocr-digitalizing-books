package com.example.ocr_digital.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.ocr_digital.helpers.ActivityStarterHelper

@Composable
fun SettingsScreen(settingsViewModel: SettingsViewModel) {
    Scaffold { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Column {
                Text(text = "Settings")
                Button(onClick = settingsViewModel::signOut) {
                    Text(text = "Sign Out")
                }
            }
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    val activityStarterHelper = ActivityStarterHelper(LocalContext.current)
    val settingsViewModel = SettingsViewModel(activityStarterHelper = activityStarterHelper)
    SettingsScreen(settingsViewModel = settingsViewModel)
}