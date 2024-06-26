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
import compose.icons.FeatherIcons
import compose.icons.feathericons.FileText
import compose.icons.feathericons.Users

enum class NavigationScreens {
    HOME,
    SCAN,
    USERS,
    SETTINGS
}

@Composable
fun NavigationScreen(
    homeScreen: @Composable () -> Unit,
    scanScreen: @Composable () -> Unit,
    usersScreen: @Composable () -> Unit,
    settingsScreen: @Composable () -> Unit,
    isAdmin: Boolean = false,
) {
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
                                Icon(FeatherIcons.FileText, "Scan")
                            } else {
                                Icon(FeatherIcons.FileText, "Scan")
                            }
                        },
                        label = { Text("Scan") }
                    )
                    if (isAdmin) {
                        NavigationBarItem(
                            selected = currentRoute == NavigationScreens.USERS.name,
                            onClick = {
                                navController.navigate(NavigationScreens.USERS.name)
                                currentRoute = NavigationScreens.USERS.name
                            },
                            icon = {
                                if (currentRoute == NavigationScreens.USERS.name) {
                                    Icon(FeatherIcons.Users, "Users")
                                } else {
                                    Icon(FeatherIcons.Users, "Users")
                                }
                            },
                            label = { Text(text = "Users") }
                        )
                    }
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
                composable(NavigationScreens.USERS.name) { usersScreen() }
                composable(NavigationScreens.SETTINGS.name) { settingsScreen() }
            }
        }
    }
}