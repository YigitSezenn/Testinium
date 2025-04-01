package com.tapandtest.app.AppNavHost

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import com.tapandtest.app.AppNavHost.AppNavigation.RegisterScreen
import com.tapandtest.app.Screens.RegisterScreen
import java.util.Objects

// `enum class (enumeration), sabit değerlerin bir koleksiyonunu tanımlamak için kullanılır. Yani, belirli ve değişmeyen değerleri temsil eden bir veri türüdür.

enum class AppNavigation {
    RegisterScreen, // Kayıt ekranını temsil eden bir enum değeri.
    SplashScreen,
    LoginScreen,// Splash ekranını temsil eden bir enum değeri.
    BaseScreen
}
sealed class NavigationItem(val route: String) {

    // RegisterScreen için route
    object RegisterScreen : NavigationItem(AppNavigation.RegisterScreen.name)

    // SplashScreen için route
    object SplashScreen : NavigationItem(AppNavigation.SplashScreen.name)

    object  LoginScreen : NavigationItem(AppNavigation.LoginScreen.name)

    object  BaseScreen : NavigationItem(AppNavigation.BaseScreen.name)

}
