package com.tapandtest.app.AppNavHost



import TimerViewModel
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.tapandtest.app.AppNavHost.NavigationItem.RegisterScreen
import com.tapandtest.app.Screens.BaseScreen
import com.tapandtest.app.Screens.LoginScreen
import com.tapandtest.app.Screens.ProfileScreen


import com.tapandtest.app.Screens.RegisterScreen
import com.tapandtest.app.firebaseviewmodel.AuthViewModel

enum class ScaleTransitionDirection {
    INWARDS, OUTWARDS
}
fun scaleIntoContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.INWARDS,
    initialScale: Float = if (direction == ScaleTransitionDirection.OUTWARDS) 0.9f else 1.1f
): EnterTransition {
    return scaleIn(
        animationSpec = tween(300, delayMillis = 100),
        initialScale = initialScale
    ) + fadeIn(animationSpec = tween(300, delayMillis = 100))
}

fun scaleOutOfContainer(
    direction: ScaleTransitionDirection = ScaleTransitionDirection.OUTWARDS,
    targetScale: Float = if (direction == ScaleTransitionDirection.INWARDS) 0.9f else 1.1f
): ExitTransition {
    return scaleOut(
        animationSpec = tween(
            durationMillis = 300,
            delayMillis = 100
        ), targetScale = targetScale
    ) + fadeOut(tween(delayMillis = 100))
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@JvmOverloads
fun AppNavHost(
    modifier: Modifier= Modifier,
    navController: NavHostController,
    startDestination: String= RegisterScreen.route,
    viewModel: AuthViewModel,
    timerViewModel: TimerViewModel, // <- Buraya ekle


    )


{
   NavHost (

       navController = navController,
       startDestination = startDestination,
       modifier = modifier,
       enterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.INWARDS) },
       exitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.OUTWARDS) },
       popEnterTransition = { scaleIntoContainer(direction = ScaleTransitionDirection.OUTWARDS) },
       popExitTransition = { scaleOutOfContainer(direction = ScaleTransitionDirection.INWARDS) }


       ) {
         composable(RegisterScreen.route) {
              RegisterScreen(navController, viewModel)
         }


       composable(NavigationItem.LoginScreen.route) {
           LoginScreen(navController)
       }
       composable(NavigationItem.BaseScreen.route) {
           BaseScreen(navController, timerViewModel)
       }
       composable(NavigationItem.ProfileScreen.route) {
           ProfileScreen(navController, viewModel,timerViewModel)
       }

    }


}