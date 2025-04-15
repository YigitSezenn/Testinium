package com.tapandtest.app.Screens

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.R

@Composable
fun BaseScreen (navController: NavController){
    val context = LocalContext.current
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("last_screen", Context.MODE_PRIVATE)
  var firebaseAuth= FirebaseAuth .getInstance()




    Text(
        text = "Base Screen",

    )
    IconButton(
        onClick =
            {
                firebaseAuth.signOut()
                navController.navigate(NavigationItem.LoginScreen.route) {
                    popUpTo("login_screen") { inclusive = true }
                }

                // Profil resmine tıklandığında yapılacak işlemler
                // Örneğin, profil resmi yükleme işlemi

            }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "Default Profile Image",
            modifier = Modifier.fillMaxSize(),
        )

    }

    
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