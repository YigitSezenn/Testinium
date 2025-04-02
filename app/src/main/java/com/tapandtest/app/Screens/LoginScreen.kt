package com.tapandtest.app.Screens

import GoogleAuthUiClient
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

import com.google.android.gms.auth.api.identity.Identity
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.R
import com.tapandtest.app.firebaseviewmodel.AuthViewModel


import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ContextCastToActivity")
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel(),
) {
    val context = LocalContext.current
    val oneTapClient = remember { Identity.getSignInClient(context) }
    val googleAuthUiClient = remember { GoogleAuthUiClient(context, oneTapClient) }
    val coroutineScope = rememberCoroutineScope()

    // Updated googleSignInLauncher
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            Log.d("GoogleSignIn", "Result OK")
            result.data?.let { intent ->
                coroutineScope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(intent)

                    if (signInResult.data != null) {
                        navController.navigate(NavigationItem.BaseScreen.route)
                    } else {
                        Toast.makeText(
                            context,
                            "Giriş başarısız: ${signInResult.error}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        } else {
            Log.d("GoogleSignIn", "Result Failed: ${result.resultCode}")
        }
    }

    var Eposta by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // SharedPreferences to retrieve the user's name
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    val name = sharedPreferences.getString("name", "") ?: ""
    var currentLocale = sharedPreferences.getString("language", "") ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(R.drawable.loginscreen),
            contentDescription = "LoginScreen",
            Modifier.size(250.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getString(context, R.string.welcome_back, currentLocale, "$name"),
            color = AppColors.TextDarkPurple,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 20.sp)
        )

        // Email TextField
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            value = Eposta,
            onValueChange = { Eposta = it },
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = AppColors.TextDarkPurple,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = getString(context, R.string.email_text, currentLocale, "E-posta"),
                        color = AppColors.TextDarkPurple,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AppColors.TextDarkPurple,
                unfocusedIndicatorColor = AppColors.TextDarkBrown
            )
        )

        // Password TextField
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            value = password,
            onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        tint = AppColors.TextDarkPurple,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text(
                        text = getString(context, R.string.password_text, currentLocale, "Şifre"),
                        color = AppColors.TextDarkPurple,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AppColors.TextDarkPurple,
                unfocusedIndicatorColor = AppColors.TextDarkBrown
            )
        )

        // Login Button
        Button(

                onClick = {
                    //login işlemleri
                    navController.navigate(NavigationItem.BaseScreen.route) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = AppColors.ButtonAccent,
                    contentColor = Color.White,
                    disabledContainerColor = AppColors.ButtonDisabled
                ),
            contentPadding = ButtonDefaults.ContentPadding,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
        ) {
            Text(text = getString(context, R.string.login_text, currentLocale, "Giriş Yap"), color = AppColors.TextPrimary)
        }

        // Google Sign-In Button
        OutlinedButton(
            onClick = {
                coroutineScope.launch {
                    val intentSender = googleAuthUiClient.signIn()
                    intentSender?.let {
                        val intent = IntentSenderRequest.Builder(it).build()
                        googleSignInLauncher.launch(intent)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = AppColors.TextDarkPurple,
                disabledContainerColor = AppColors.ButtonDisabled
            ),
            contentPadding = ButtonDefaults.ContentPadding,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .height(50.dp)
        ) {
            Image(
                painterResource(R.drawable.google),
                contentDescription = "Google",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = getString(context, R.string.google_text, currentLocale, "Google ile giriş yap"),
                color = AppColors.TextDarkPurple,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp)
            )
        }

        // Register Button
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = getString(context, R.string.registernav_text, currentLocale, "Hesabın yok mu? Kayıt ol!"),
            color = AppColors.TextDarkPurple,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .clickable {
                    navController.navigate(NavigationItem.RegisterScreen.route)
                }
        )
    }
}


fun getString(context: Context, resId: Int, localeCode: String, vararg formatArgs: Any): String {
    val config = context.resources.configuration
    config.setLocale(Locale(localeCode)) // Kullanıcının seçtiği dili ayarla
    val localizedContext = context.createConfigurationContext(config)
    return localizedContext.resources.getString(resId, *formatArgs)
}
