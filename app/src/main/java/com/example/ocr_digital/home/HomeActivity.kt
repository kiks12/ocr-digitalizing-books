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
import com.example.ocr_digital.R
import com.example.ocr_digital.api.UsersAPI
import com.example.ocr_digital.folder.FolderUtilityViewModel
import com.example.ocr_digital.helpers.ActivityStarterHelper
import com.example.ocr_digital.helpers.RetrofitHelper
import com.example.ocr_digital.helpers.ToastHelper
import com.example.ocr_digital.models.ResponseStatus
import com.example.ocr_digital.models.UserInformation
import com.example.ocr_digital.navigation.NavigationScreen
import com.example.ocr_digital.onboarding.OnBoardingActivity
import com.example.ocr_digital.onboarding.walkthrough.WalkthroughActivity
import com.example.ocr_digital.repositories.UsersRepository
import com.example.ocr_digital.scan.ScanScreen
import com.example.ocr_digital.scan.ScanViewModel
import com.example.ocr_digital.settings.SettingsScreen
import com.example.ocr_digital.settings.SettingsViewModel
import com.example.ocr_digital.startup.StartupActivity
import com.example.ocr_digital.ui.theme.OcrDigitalTheme
import com.example.ocr_digital.users.UsersScreen
import com.example.ocr_digital.users.UsersViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private val usersRepository = UsersRepository()
    private val auth = Firebase.auth

    private lateinit var scanViewModel: ScanViewModel

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser ?: return

        lifecycleScope.launch {
            val response = usersRepository.getUser(currentUser.uid)
            if (response.status == ResponseStatus.SUCCESSFUL) {
                val userData = response.data["user"] as List<*>
                if (userData.isEmpty()) startStartupActivity()
                val user = userData[0] as UserInformation
                if (user.onboarding) return@launch startOnBoardingActivity()
                if (user.walkthrough) return@launch startWalkthroughActivity()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        scanViewModel.refresh()
    }

    private fun startStartupActivity() {
        val intent = Intent(this, StartupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startOnBoardingActivity() {
        val intent = Intent(this, OnBoardingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun startWalkthroughActivity() {
        val intent = Intent(this, WalkthroughActivity::class.java)
        intent.putExtra("AUTOMATIC", true)
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val toastHelper = ToastHelper(this)
        val activityStarterHelper = ActivityStarterHelper(this)
        val settingsViewModel = SettingsViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)
        val homeViewModel = HomeViewModel(
            activityStarterHelper,
            getString(R.string.home_authenticated),
            getString(R.string.home_unauthenticated)
        )
        scanViewModel = ScanViewModel(toastHelper, activityStarterHelper)
        val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)
        val usersApi = RetrofitHelper.getInstance().create(UsersAPI::class.java)
        val usersViewModel = UsersViewModel(usersApi, toastHelper)

        setContent {
            OcrDigitalTheme {
                Scaffold { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavigationScreen(
                            email = auth.currentUser?.email?: "",
                            homeScreen = { HomeScreen(homeViewModel = homeViewModel) },
                            scanScreen = { ScanScreen(scanViewModel = scanViewModel, folderUtilityViewModel = folderUtilityViewModel) },
                            usersScreen = { UsersScreen(usersViewModel = usersViewModel) },
                            settingsScreen = { SettingsScreen(settingsViewModel = settingsViewModel) }
                        )
                    }
                }
            }
        }

        supportActionBar?.hide()
    }
}