package com.tapandtest.app.Screens

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun BaseScreen (navController: NavController){
    val context = LocalContext.current
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)



    
}


//@Composable
//fun AppNavBar
//(
//
//) {
//    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//    val navigationItems = MainNavigation::class.nestedClasses.map {
//        it.objectInstance as MainNavigation
//    }
//    var selectedItem by remember { mutableIntStateOf(0) }
//    // Your AppBar implementation here
//    // You can use the navController to navigate between screens
//    // and call onThemeToggle() to toggle the theme.
//} burada kaldım 