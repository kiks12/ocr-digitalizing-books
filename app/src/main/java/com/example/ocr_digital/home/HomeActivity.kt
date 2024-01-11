package com.example.ocr_digital.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import com.example.ocr_digital.folder.FolderUtilityViewModel
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.navigation.NavigationScreen
import com.example.ocr_digital.repositories.UsersRepository
import com.example.ocr_digital.settings.SettingsScreen
import com.example.ocr_digital.settings.SettingsViewModel
import com.example.ocr_digital.startup.StartupActivity
import com.example.ocr_digital.ui.theme.OcrdigitalTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser ?: return

        lifecycleScope.launch {
            val response = usersRepository.getUser(currentUser.uid)
            if (response.status == ResponseStatus.SUCCESSFUL) {
                val user = response.data["user"] as List<*>
                if (user.isEmpty()) startStartupActivity()
            }
        }
    }

    private fun startStartupActivity() {
        val intent = Intent(this, StartupActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val activityStarterHelper = ActivityStarterHelper(this)
        val settingsViewModel = SettingsViewModel(activityStarterHelper = activityStarterHelper)
        val homeViewModel = HomeViewModel(activityStarterHelper = activityStarterHelper)
        val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

        setContent {
            OcrdigitalTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationScreen(
                            homeScreen = { HomeScreen(homeViewModel = homeViewModel, folderUtilityViewModel = folderUtilityViewModel) },
                            settingsScreen = { SettingsScreen(settingsViewModel = settingsViewModel) }
                        )
                    }
                }
            }
        }
    }
}