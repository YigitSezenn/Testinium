package com.tapandtest.app.firebaseviewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tapandtest.app.AppNavHost.NavigationItem
import kotlin.math.log

class AuthViewModel : ViewModel() {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    fun loginviewModel(
        Email: String,
        Password: String,
        callback: (String) -> Unit,
    ) {
        auth.signInWithEmailAndPassword(
            Email,Password

        ).addOnCompleteListener {
            login  ->
            if(login.isSuccessful)
            {
                callback ("Giriş Başarılı")
            }
            else
            {
                val exception = login.exception
                when(exception)
                {
                    is FirebaseAuthInvalidUserException -> {
                        callback("Mail Adresi Bulunamadı")
                    }
                    is FirebaseAuthInvalidCredentialsException -> {
                        callback("Geçersiz Mail Adresi veya Şifre")
                    }
                    else -> callback("Hata: ${login.exception?.message}")
                }
            }
        }


    }


    fun RegisterViewModel(
        Email: String,
        Password: String,
        callback: (String) -> Unit,


        ) {

        auth.createUserWithEmailAndPassword(
            Email, Password
        ).addOnCompleteListener { register ->

            if (register.isSuccessful) {
                callback("Kayıt Başarılı")

            } else {
                val exception = register.exception
                when
                        (exception) {
                    is FirebaseAuthInvalidUserException -> {
                        callback("Mail Adresi Zaten Kullanımda")
                    }

                    is FirebaseAuthInvalidCredentialsException -> {
                        callback(" Geçersiz Mail Adresi veya Şifre")
                    }

                    else ->
                        callback("Hata: ${register.exception?.message}")

                }


            }


        }

    }

    fun resetPassword(email: String) {

    }
}
