package com.tapandtest.app.Screens

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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppColor.DropDownData
import com.tapandtest.app.AppNavHost.NavigationItem

import com.tapandtest.app.R
import com.tapandtest.app.firebaseviewmodel.AuthViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel

) {
    val context = LocalContext.current
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("myPrefs",
            Context.MODE_PRIVATE)


    var name by remember { mutableStateOf("") } // Başlangıçta tamamen boş
    var Eposta by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var expended by remember { mutableStateOf(false) }
    val developerList =
        DropDownData.developerlist // developerList -> DropDownData sınıfındaki developerlist listesini alır.
    var selectedDeveloper by remember { mutableStateOf("") }
    // selectedDeveloper -> Seçilen geliştiriciyi alır.

    // developerlist.first()["name"].toString() ->
    // developerlist listesinin ilk elemanının adını alır.


    // RegisterScreen ekranının tasarımı burada yapılacak.


    Column(
        modifier = Modifier
            .fillMaxSize() //  Ekranın tamamını kaplar.

            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center


    )

    {

        Image(
            painterResource(R.drawable.registerscreen),
            contentDescription = "RegisterScreen",
            Modifier.size(250.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Test süreçlerini takip et," +
                    "hataları kaydet ve \ngörevlerini yönet.Hemen kaydol!",
            color = AppColors.TextDarkPurple,
            style = TextStyle(fontSize = 16.sp)
        )

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
                            "Lütfen Alanınızı Seçiniz"


                        )


                    }
                },

                readOnly = true,

                modifier = Modifier
                    .padding(8.dp)
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









        OutlinedTextField(
            modifier = Modifier
                .padding(4.dp),
            value = name,


            onValueChange = {
                name = it
            },


            label = {
                Row(verticalAlignment = Alignment.CenterVertically) {

                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null,
                        tint = AppColors.TextDarkPurple,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Ad Soyad")

                }


            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AppColors.TextDarkPurple,
                unfocusedBorderColor = AppColors.TextDarkBrown
            )

        )

        OutlinedTextField(
            modifier = Modifier
                .padding(8.dp),
            value = Eposta,
            onValueChange = {
                Eposta = it
            },
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = AppColors.TextDarkPurple,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    Text("Eposta")

                }


            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // Arka plan rengi
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AppColors.TextDarkPurple, // Çerçeve rengi
                unfocusedIndicatorColor = AppColors.TextDarkBrown
            )
        )
        OutlinedTextField(
            modifier = Modifier
                .padding(4.dp),
            value = password,
            onValueChange = {
                password = it
            },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = AppColors.TextDarkPurple,
                        modifier = Modifier.size(24.dp),
                    )
                    Spacer(modifier = Modifier.size(8.dp))

                    Text("Şifre")

                }


            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,  // Arka plan rengi
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = AppColors.TextDarkPurple, // Çerçeve rengi
                unfocusedIndicatorColor = AppColors.TextDarkBrown
            )
        )

        Button(
            onClick = {
                if (name.isNotEmpty() && Eposta.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.RegisterViewModel(Eposta, password) { message ->

                        Toast.makeText(context, message, Toast.LENGTH_SHORT)
                            .show()
                        if (message == "Kayıt Başarılı") {

                            sharedPreferences.edit().putString("name", name).apply()
                            println("Name before registration: $name") // Logcat'te görünür
                            navController.navigate(NavigationItem.LoginScreen.route)

                        }

                    }






                    Eposta = "" // Input'u temizle
                    password = "" // Input'u temizle
                } else {
                    Toast.makeText(context, "Lütfen tüm alanları doldurunuz", Toast.LENGTH_SHORT)
                        .show()

                }


            },
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.ButtonAccent, // Normal hali
                contentColor = Color.White, // İçindeki yazı rengi
                disabledContainerColor = AppColors.ButtonDisabled,


                ),

            contentPadding = ButtonDefaults.ContentPadding,


            modifier = Modifier
                .padding(
                    8.dp
                )
                .height(50.dp)
                .width(300.dp)


        ) {
            Text(
                text = "Kayıt ol",
                color = AppColors.TextPrimary

            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Zaten bir hesabınız var mı? Giriş yapın",
            color = AppColors.TextDarkPurple,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .clickable {
                    navController.navigate(NavigationItem.LoginScreen.route)
                }
        )

        // RegisterScreen ekranının tasarımı burada yapılacak.


    }
}

// RegisterScreen ekranı için gerekli olan işlemler burada yapılacak.


