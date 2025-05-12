package com.tapandtest.app

import android.R.attr.onClick
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.balltrajectory.Straight
import com.exyte.animatednavbar.animation.balltrajectory.Teleport
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.skydoves.landscapist.rememberDrawablePainter
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.AppNavHost
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.ui.theme.TapAndTestTheme
import org.checkerframework.checker.units.qual.Current
import kotlin.apply

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            AppContent()

        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable

private fun AppContent() {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    val navController = rememberNavController()

    val lastScreen = sharedPreferences.getString("last_screen", NavigationItem.RegisterScreen.route) ?: NavigationItem.BaseScreen.route //

    var isDarkTheme by remember {
        mutableStateOf(sharedPreferences.getBoolean("dark_mode", false))
    }

    val currentRoute = navController.currentBackStackEntryAsState().value
    val showNavigationBar = currentRoute?.destination?.route !in listOf(
        NavigationItem.RegisterScreen.route,
        NavigationItem.LoginScreen.route
    )
    val shouldShowThemeToggle = currentRoute?.destination?.route == NavigationItem.ProfileScreen.route //

    TapAndTestTheme(darkTheme = isDarkTheme) {
        LaunchedEffect(isDarkTheme) {
            sharedPreferences.edit { putBoolean("dark_mode", isDarkTheme) }
        }
        LaunchedEffect(navController) {
            navController.currentBackStackEntryFlow.collect { entry ->
                entry?.destination?.route?.let { route ->
                    sharedPreferences.edit { putString("last_screen", route) }
                }
            }
        }

        Scaffold(
            topBar = {
                AppTopBar(
                    isDarkTheme = isDarkTheme,
                    shouldShowThemeToggle = shouldShowThemeToggle,
                    onThemeToggle = { isDarkTheme = !isDarkTheme }
                )
            },
            bottomBar = {
                if (showNavigationBar) {
                    BottomNavigationBar(navController = navController)
                }
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel(),
                startDestination = lastScreen,
                timerViewModel = viewModel() // <- Buraya ekle
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
        title = { Text("FocusFlow") },
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

@SuppressLint("UseCompatLoadingForDrawables")
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
enum class NavigationBarItems(val title: String, val icon: ImageVector) {
    Person(
        title = "Kişi",
        icon = Icons.Default.Person,

    ),
    Settings(
        title = "Ayarlar",
        icon = Icons.Default.Settings
    );
}




@Composable
fun BottomNavigationBar(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = remember {
        context.getSharedPreferences("last_screen", Context.MODE_PRIVATE)
    }

    val navigationBarItems = NavigationBarItems.values().toList()

    var selectedIndex by remember {
        mutableStateOf(sharedPreferences.getInt("last_selected_index", 0)) // varsayılan olarak 0 döner
    }

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route // current route'u al

    // Güncel route'a göre index'i ayarla
    LaunchedEffect(currentRoute) {
        val index = when (currentRoute) { // currentRoute null olursa 0 döner
            NavigationItem.BaseScreen.route -> 0
            NavigationItem.ProfileScreen.route -> 1
            else -> 0 // Varsayılan olarak 0 döner
        }
        selectedIndex = index
    }

    AnimatedNavigationBar(
        modifier = Modifier
            .height(64.dp)
            .padding(2.dp)
            .offset(y = -32.dp),
        selectedIndex = selectedIndex,
        cornerRadius = shapeCornerRadius(cornerRadius = 32.dp),
        ballColor = AppColors.PrimaryPurple,
        ballAnimation = Parabolic(tween(durationMillis = 300)),
        indentAnimation = Height(tween(durationMillis = 300)),
        barColor = AppColors.TextDarkPurple,
    ) {
        navigationBarItems.forEachIndexed { index, item ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable {
                        if (selectedIndex != index) { // Seçili index değiştiyse
                            selectedIndex = index // Seçili index'i güncelle
                            sharedPreferences.edit {
                                putInt("last_selected_index", index) // Seçili index'i kaydet
                            }

                            when (item) {
                                NavigationBarItems.Person -> {
                                    navController.navigate(NavigationItem.BaseScreen.route) {
                                        popUpTo(0)
                                    }
                                }


                                NavigationBarItems.Settings -> {
                                    navController.navigate(NavigationItem.ProfileScreen.route) {
                                        popUpTo(0) // Geri gitme işlemi
                                    }
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    modifier = Modifier.size(26.dp),
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = if (selectedIndex == index)
                        AppColors.PastelPurple
                    else
                        AppColors.Background
                )
            }
        }
    }
}



