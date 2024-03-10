package com.example.ocr_digital.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class NavigationScreens {
    HOME,
    SCAN,
    SETTINGS
}

@Composable
fun NavigationScreen(homeScreen: @Composable () -> Unit, scanScreen: @Composable () -> Unit, settingsScreen: @Composable () -> Unit) {
    val navController = rememberNavController()
    var currentRoute by remember {
        mutableStateOf(NavigationScreens.HOME.name)
    }

    Scaffold(
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == NavigationScreens.HOME.name,
                        onClick = {
                            navController.navigate(NavigationScreens.HOME.name)
                            currentRoute = NavigationScreens.HOME.name
                        },
                        icon = {
                            if (currentRoute == NavigationScreens.HOME.name) {
                                Icon(Icons.Default.Home, "Home")
                            } else {
                                Icon(Icons.Outlined.Home, "Home")
                            }
                        },
                        label = { Text(text = "Home") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == NavigationScreens.SCAN.name,
                        onClick = {
                            navController.navigate(NavigationScreens.SCAN.name)
                            currentRoute = NavigationScreens.SCAN.name
                        },
                        icon = {
                            if (currentRoute == NavigationScreens.SCAN.name) {
                                Icon(documentScannerFilled(), "Scan")
                            } else {
                                Icon(documentScannerOutlined(), "Scan")
                            }
                        }
                    )
                    NavigationBarItem(
                        selected = currentRoute == NavigationScreens.SETTINGS.name,
                        onClick = {
                            navController.navigate(NavigationScreens.SETTINGS.name)
                            currentRoute = NavigationScreens.SETTINGS.name
                        },
                        icon = {
                            if (currentRoute == NavigationScreens.SETTINGS.name) {
                                Icon(Icons.Default.Settings, "Settings")
                            } else {
                                Icon(Icons.Outlined.Settings, "Settings")
                            }
                        },
                        label = { Text(text = "Settings") }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(
                navController = navController,
                startDestination = NavigationScreens.HOME.name
            ) {
                composable(NavigationScreens.HOME.name) { homeScreen() }
                composable(NavigationScreens.SCAN.name) { scanScreen() }
                composable(NavigationScreens.SETTINGS.name) { settingsScreen() }
            }
        }
    }
}