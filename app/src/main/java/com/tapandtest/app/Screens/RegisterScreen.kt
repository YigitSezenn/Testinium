package com.tapandtest.app.Screens

import DatabaseViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppColor.DropDownData
import com.tapandtest.app.AppNavHost.NavigationItem

import com.tapandtest.app.R
import com.tapandtest.app.firebaseviewmodel.AuthViewModel
import java.util.Locale
import androidx.core.content.edit
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth


@SuppressLint("UseKtx")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel
) {
     val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

    var name by remember { mutableStateOf("") }
    var Eposta by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expended by remember { mutableStateOf(false) }

    val viewModel1: DatabaseViewModel = viewModel()
    var selectedDeveloper by remember { mutableStateOf("") }
    var currentLocale by remember {
        mutableStateOf(
            sharedPreferences.getString(
                "language",
                Locale.getDefault().language
            ) ?: "en"
        )
    }

    val developerList = DropDownData.developerlist

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.registerscreen),
            contentDescription = "RegisterScreen",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Orantılı boyutlandırma
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getString(context, R.string.register_welcome_text, localeCode = currentLocale),
            color = AppColors.TextDarkPurple,
            style = TextStyle(fontSize = 16.sp)
        )

        // Dropdown Menü
        ExposedDropdownMenuBox(
            expanded = expended,
            onExpandedChange = { expended = !expended },
        )
        {
            OutlinedTextField(
                value = selectedDeveloper,
                onValueChange = {
                    selectedDeveloper = it //  Seçilen geliştiriciyi alır.
                },
                label = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = AppColors.TextDarkPurple,
                            modifier = Modifier.size(24.dp),
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                        Text(
                            text = getString(
                                context,
                                R.string.combobox_text,
                                localeCode = currentLocale,
                            ),
                            color = AppColors.TextDarkPurple,
                            style = TextStyle(fontSize = 16.sp)


                        )


                    }
                },


                readOnly = true,

                modifier = Modifier

                    .fillMaxWidth()
                    .menuAnchor() //  Menüyü açarken TextField'ın altında olmasını sağlar.s
                    .clickable { expended = !expended } //  Menü aç/kapat


            )
            ExposedDropdownMenu(
                expanded = expended,
                onDismissRequest = { expended = false },
            ) {
                developerList.forEach {
                    DropdownMenuItem(
                        text = { Text(it["name"].toString()) },
                        onClick = {
                            selectedDeveloper = it["name"].toString()
                            expended = false
                        }
                    )
                }
            }
        }


        // Input Fields
        OutlinedTextField(
            value = name,
            onValueChange = {newValue ->
                // Sadece harfleri kabul et (rakamları sil)
                val filtered = newValue.filter { it.isLetter() || it.isWhitespace() }
                name = filtered
      },
            label = { Text(getString(context, R.string.name_input, localeCode = currentLocale)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.AccountBox,
                    contentDescription = null,
                    tint = AppColors.TextDarkPurple
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.TextDarkPurple,
                unfocusedBorderColor = AppColors.TextDarkBrown
            )
        )

        OutlinedTextField(
            value = Eposta,
            onValueChange = { Eposta = it },
            label = { Text(getString(context, R.string.email_text, localeCode = currentLocale)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = AppColors.TextDarkPurple
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.TextDarkPurple,
                unfocusedBorderColor = AppColors.TextDarkBrown
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {
                Text(
                    getString(
                        context,
                        R.string.password_text,
                        localeCode = currentLocale,
                    )
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = AppColors.TextDarkPurple
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.TextDarkPurple,
                unfocusedBorderColor = AppColors.TextDarkBrown
            )
        )

        Button(
            onClick = {
                if (name.isNotEmpty() && Eposta.isNotEmpty() && password.isNotEmpty()) {

                        if (password.length < 6) {
                            Toast.makeText(
                                context, R.string.passwordlength_text, Toast.LENGTH_SHORT


                            ).show()
                        } else if (selectedDeveloper.isEmpty()) {
                            Toast.makeText(
                                context,
                                R.string.combobox_text,
                                Toast.LENGTH_SHORT
                            ).show()
                        }   else {

                            viewModel.RegisterViewModel(Eposta, password) { message ->

                                if (message == "Kayıt Başarılı") {
                                    val user = DatabaseViewModel.User(
                                        userId = auth.currentUser?.uid.toString(),
                                        username = name,
                                        email = Eposta,
                                        job = selectedDeveloper,
                                        password = password,
                                        bio = "",
                                       // profilePictureUrl =
                                        //createdAt = System.currentTimeMillis().toString()
                                    )
                                    viewModel1.addUser(user)
                                    // Kayıt başarılıysa ad kaydedilir ve login ekranına yönlendirilir
                                    sharedPreferences.edit().putString("name", name).apply()
//                                    sharedPreferences.edit().putString(
//                                        "developer",
//                                        selectedDeveloper
//                                    ).apply()
                                    sharedPreferences.edit()
                                        .putString("last_screen", NavigationItem.LoginScreen.route).apply()
                                    navController.navigate(NavigationItem.LoginScreen.route)
                                }
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(context, R.string.allinputs_null, Toast.LENGTH_SHORT)
                        .show()
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.ButtonAccent,
                contentColor = Color.White
            )
        ) {
            Text(
                text = getString(
                    context,
                    R.string.registerbutton_text,
                    localeCode = currentLocale,
                ),
                color = AppColors.TextPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = getString(context, R.string.loginav_text, localeCode = currentLocale),
            color = AppColors.TextDarkPurple,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                navController.navigate(NavigationItem.LoginScreen.route)
            }
        )

        // Dil Seçimi
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextButton(onClick = {
                currentLocale = "en"
                sharedPreferences.edit().putString("language", currentLocale).apply()
            }) {
                Text("EN", color = AppColors.TextDarkPurple)
            }
            TextButton(onClick = {
                currentLocale = "tr"
                sharedPreferences.edit().putString("language", currentLocale).apply()
            }) {
                Text("TR", color = AppColors.TextDarkPurple)
            }
        }
    }
}


