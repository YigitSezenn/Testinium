package com.tapandtest.app.Screens

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation.Url
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.R
import com.tapandtest.app.firebaseviewmodel.AuthViewModel
import org.koin.core.component.getScopeName
import java.io.File
import kotlin.math.sign

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel




) { var bio by remember { mutableStateOf("") }
    var FirstName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var ProfileImage by remember { mutableStateOf<Uri?>(null) }
    var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current
    val sharedPreferences =
        LocalContext.current.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    var currentLocale = sharedPreferences.getString("language", "") ?: ""
    val name = sharedPreferences.getString("name", "") ?: ""
   val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    LaunchedEffect(Unit) {
        FirstName = name
        email = firebaseAuth.currentUser?.email ?: ""


    }
    // Ana ekran düzeni (dikey olarak sıralanmış)
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        IconButton(
            onClick = {
                // Profil resmine tıklandığında yapılacak işlemler
                // Örneğin, profil resmi yükleme işlemi
            },
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        ) {
            imageBitmap?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } ?: run {
                Image(
                    painter = painterResource(R.drawable.ic_launcher_foreground),
                    contentDescription = "Default Profile Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

            }
        }

        Button(
            onClick = {
                //Todo: Profil resmi yükleme işlemi
            }
        ) {
            Text(text =getString(context,R.string.AddImage,currentLocale,"Add Image"))

        }


        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxSize()
        )
            {

                OutlinedTextField(
                    value = FirstName,
                    onValueChange = { FirstName = it },
                 //    firebaseAuth.currentUser?.displayName(name), // Adı güncelle
                    label = { Text("Adınız Soyadınız") }, // veya dilediğin metin


                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it  // E-posta adresini güncelle
                    // Firebase Authentication ile güncelleyebilirsin
                        firebaseAuth .currentUser?.verifyBeforeUpdateEmail(email)

                                    },
                    label = { Text("E-posta") }, // veya dilediğin metin
                    singleLine = true,

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Hakkında") }, // veya dilediğin metin
                    singleLine = false,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
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

    }

}

