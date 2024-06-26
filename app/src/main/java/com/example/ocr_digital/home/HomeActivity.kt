package com.example.ocr_digital.home

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
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
    private val loading = mutableStateOf(true)

    private lateinit var scanViewModel: ScanViewModel

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = applicationContext.getSystemService(CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetworkInfo?.isConnected == true
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser ?: return

        lifecycleScope.launch {
            val response = usersRepository.getUser(currentUser.uid)
            if (response.status == ResponseStatus.SUCCESSFUL) {
                val userData = response.data["user"] as List<*>
                if (userData.isEmpty()) {
                    startStartupActivity()
                } else {
                    val user = userData[0] as UserInformation
                    if (user.onboarding) return@launch startOnBoardingActivity()
                    if (user.walkthrough) return@launch startWalkthroughActivity()
                }
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
        val toastHelper = ToastHelper(this@HomeActivity)
        val activityStarterHelper = ActivityStarterHelper(this@HomeActivity)
        scanViewModel = ScanViewModel(toastHelper, activityStarterHelper)

        lifecycleScope.launch {
            val settingsViewModel = SettingsViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

            val homeViewModel = HomeViewModel(
                activityStarterHelper,
                getString(R.string.home_authenticated),
                getString(R.string.home_unauthenticated)
            )
            val folderUtilityViewModel = FolderUtilityViewModel(toastHelper = toastHelper, activityStarterHelper = activityStarterHelper)

            val usersApi = RetrofitHelper.getInstance(::isNetworkAvailable)?.create(UsersAPI::class.java)
            val usersViewModel = UsersViewModel(usersApi!!, toastHelper, activityStarterHelper)

            val usersResponse = usersRepository.getUser(auth.currentUser?.uid ?: "")
            val user : UserInformation?
            if ((usersResponse.data["user"] as List<*>).isNotEmpty()) {
                user = (usersResponse.data["user"] as List<*>)[0] as UserInformation
                scanViewModel.setAdmin(user.admin)
            } else {
                user = null
                scanViewModel.setAdmin(false)
            }
            loading.value = false

            setContent {
                OcrDigitalTheme {
                    Scaffold { innerPadding ->
                        if (loading.value) {
                            Box(modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()) {
                                CircularProgressIndicator()
                            }
                        } else {
                            Box(modifier = Modifier.padding(innerPadding)) {
                                NavigationScreen(
                                    homeScreen = { HomeScreen(homeViewModel = homeViewModel) },
                                    scanScreen = { ScanScreen(scanViewModel = scanViewModel, folderUtilityViewModel = folderUtilityViewModel) },
                                    usersScreen = { UsersScreen(usersViewModel = usersViewModel) },
                                    settingsScreen = { SettingsScreen(settingsViewModel = settingsViewModel) },
                                    isAdmin = user?.admin ?: false
                                )
                            }
                        }
                    }
                }
            }
        }

        supportActionBar?.hide()
    }
}