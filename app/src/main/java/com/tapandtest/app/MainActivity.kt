package com.tapandtest.app

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.skydoves.landscapist.rememberDrawablePainter
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.AppNavHost
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.ui.theme.TapAndTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppContent()
        }
    }
}

@Composable
private fun AppContent() {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE) }
    val navController = rememberNavController()

    // Theme state and preference handling
    var isDarkTheme by remember { mutableStateOf(sharedPreferences.getBoolean("dark_mode", false)) }

    // Determine whether to show the theme toggle button
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Determine whether to show the theme toggle button (only on RegisterScreen)
    val shouldShowThemeToggle = currentRoute == NavigationItem.RegisterScreen.route
    TapAndTestTheme(darkTheme = isDarkTheme) {
        // Save the theme preference when it changes
        LaunchedEffect(isDarkTheme) {
            sharedPreferences.edit { putBoolean("dark_mode", isDarkTheme) }
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    isDarkTheme = isDarkTheme,
                    shouldShowThemeToggle = shouldShowThemeToggle,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppTopBar(
    isDarkTheme: Boolean,
    shouldShowThemeToggle: Boolean,
    onThemeToggle: () -> Unit
) {
    TopAppBar(
        title = { Text("Testinium") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isDarkTheme) Color.Black else AppColors.Background,
            titleContentColor = AppColors.PrimaryAmethyst
        ),
        actions = {
            if (shouldShowThemeToggle) {
                ThemeToggleButton(
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    )
}

@Composable
private fun ThemeToggleButton(
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val context = LocalContext.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(end = 8.dp)
    ) {
        Text(
            text = "Tema Değiştir",
            color = if (isDarkTheme) Color.White else Color.Black
        )

        IconButton(
            onClick = onThemeToggle,
            modifier = Modifier.size(48.dp)
        ) {
            Image(
                painter = rememberDrawablePainter(
                    context.resources.getDrawable(
                        if (isDarkTheme) R.drawable.darkmode else R.drawable.lightmode,
                        context.theme
                    )
                ),
                contentDescription = "Tema değiştir",
                modifier = Modifier
                    .clip(CircleShape)
                    .size(24.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}
