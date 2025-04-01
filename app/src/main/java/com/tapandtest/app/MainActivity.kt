package com.tapandtest.app

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.AppNavHost
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.ui.theme.TapAndTestTheme
import java.util.Locale
import androidx.core.content.edit
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val savedTheme = sharedPreferences.getBoolean("dark_mode", false)
            val navController = rememberNavController()
            var isDarkTheme by remember { mutableStateOf(savedTheme) }
            val visibility = remember { mutableStateOf(false) }
            val backStackEntry = navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry.value?.destination?.route
            visibility.value =currentRoute !in listOf(
                NavigationItem.LoginScreen.route,
                NavigationItem.RegisterScreen.route,

            )







            TapAndTestTheme(darkTheme = isDarkTheme) {



                DisposableEffect(isDarkTheme) {
                    sharedPreferences.edit() { putBoolean("dark_mode", isDarkTheme) }
                    onDispose {
                        // Cleanup if needed
                    }
                }
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("Testinium") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = if (isDarkTheme) Color.Black else AppColors.Background,
                                titleContentColor = AppColors.PrimaryAmethyst
                            ),

                            actions = {
                                if (visibility.value)
                                    return@TopAppBar

                                Text(text = "Dark/Light")
                                Switch(

                                    checked = isDarkTheme,

                                    onCheckedChange = {

                                        isDarkTheme = it
                                        if (it) {
                                            sharedPreferences.edit().putBoolean("dark_mode", true).apply()

                                            Toast.makeText(context, "Karanlık Mod Açık", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Karanlık Mod Kapalı", Toast.LENGTH_SHORT).show()
                                            sharedPreferences.edit().putBoolean("dark_mode", false).apply()
                                        }
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = AppColors.TextDarkPurple,
                                        uncheckedTrackColor = Color.White,
                                        checkedThumbColor = AppColors.TextPrimary,
                                        uncheckedThumbColor = AppColors.PrimaryPurple,
                                       uncheckedBorderColor = AppColors.TextDarkPurple,
                                        checkedBorderColor = AppColors.TextPrimary
                                    ),

                                    modifier = Modifier.padding(16.dp) // Switch için boşluk ekleyelim
                                )
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    // Burada isDarkTheme değişikliğini sadece gerekli ekranlarda geçirebilirsiniz.
                    AppNavHost(
                        navController = navController,
                        viewModel = viewModel(),
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
