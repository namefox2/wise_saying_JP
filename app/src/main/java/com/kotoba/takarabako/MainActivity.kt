package com.kotoba.takarabako

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.kotoba.takarabako.navigation.BottomNavBar
import com.kotoba.takarabako.navigation.NavGraph
import com.kotoba.takarabako.ui.theme.KotobaTheme
import com.kotoba.takarabako.util.NotificationHelper
import com.kotoba.takarabako.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        MobileAds.setRequestConfiguration(
            com.google.android.gms.ads.RequestConfiguration.Builder()
                .setTestDeviceIds(listOf("EDB1FC15E44D699A918000A49655C8BE"))
                .build()
        )
        MobileAds.initialize(this)
        NotificationHelper.createChannel(this)
        setContent {
            val settingsVm: SettingsViewModel = viewModel()
            val currentTheme by settingsVm.currentTheme.collectAsState()
            val fontScale by settingsVm.fontScale.collectAsState()

            KotobaTheme(theme = currentTheme, fontScale = fontScale) {
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavGraph(navController = navController)
                    }
                }
            }
        }
    }
}
