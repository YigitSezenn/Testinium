package com.tapandtest.app.Screens

import DatabaseViewModel
import TimerViewModel
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.R
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppColor.DropDownData
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.firebaseviewmodel.AuthViewModel
import java.io.ByteArrayOutputStream
import java.io.InputStream


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: AuthViewModel,
    timerViewModel: TimerViewModel,
) {
    var bio by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var profileBitmap: Bitmap? by remember { mutableStateOf(null) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    val currentLocale = sharedPreferences.getString("language", "") ?: ""
    var selectedDeveloper by remember { mutableStateOf("") }
    var menuacilis = remember { mutableStateOf(false) }
    val viewModel: TimerViewModel = viewModel()
    val developerList = DropDownData.developerlist
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val dbViewModel: DatabaseViewModel = viewModel()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            // Encode image as Base64 and update state
            val encodedImage = encodeImageToBase64(it, context)
            imageUri = it
            profileBitmap = decodeBase64ToBitmap(encodedImage)
        }
    }

    // Fetch user data when screen is loaded
    LaunchedEffect(Unit) {
        dbViewModel.getUser { user ->
            user?.let {
                firstName = it.username.orEmpty()
                email = it.email.orEmpty()
                bio = it.bio.orEmpty()
                selectedDeveloper = it.job.orEmpty()
                profileBitmap = decodeBase64ToBitmap(it.profilePictureUrl)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Profile Picture
        if (profileBitmap != null) {
            Image(
                bitmap = profileBitmap!!.asImageBitmap(),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.Gray, CircleShape)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(getString(context, com.tapandtest.app.R.string.no_image, localeCode = currentLocale ), color = Color.DarkGray)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(

            onClick = {
                launcher.launch("image/*")


            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = AppColors.PrimaryPurple,
                contentColor = Color.White,
            )

        ) {
            Text(text = getString(context, com.tapandtest.app.R.string.add_image, localeCode = currentLocale))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxSize()
                .height(200.dp)
                .width(400.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = getString(context, com.tapandtest.app.R.string.profile_information, localeCode = currentLocale),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Dropdown Menu for Developer Selection
                ExposedDropdownMenuBox(
                    expanded = menuacilis.value,
                    onExpandedChange = { menuacilis.value = it },
                ) {
                    OutlinedTextField(
                        value = selectedDeveloper,
                        onValueChange = { selectedDeveloper = it },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = AppColors.TextDarkPurple,
                                    modifier = Modifier.size(24.dp),
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(
                                    text =  getString(context, com.tapandtest.app.R.string.select_developer_role, localeCode = currentLocale),
                                    color = AppColors.TextDarkPurple,
                                    style = TextStyle(fontSize = 16.sp)
                                )
                            }
                        },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable { menuacilis.value = !menuacilis.value }
                    )

                    ExposedDropdownMenu(
                        expanded = menuacilis.value,
                        onDismissRequest = { menuacilis.value = false },
                    ) {
                        developerList.forEach {
                            DropdownMenuItem(
                                text = { Text(it["name"].toString()) },
                                onClick = {
                                    selectedDeveloper = it["name"].toString()
                                    menuacilis.value = false
                                }
                            )
                        }
                    }
                }

                // Text Fields
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text(text =  getString(context, com.tapandtest.app.R.string.name, localeCode = currentLocale)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text =getString(context,com.tapandtest.app.R.string.name,localeCode = currentLocale)) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text(text =getString(context,com.tapandtest.app.R.string.about, localeCode = currentLocale)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Update and Log Out Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = {
                            val user = DatabaseViewModel.User(
                                userId = firebaseAuth.currentUser?.uid,
                                username = firstName,
                                email = email,
                                password = firebaseAuth.currentUser?.email,
                                job = selectedDeveloper,
                                bio = bio,
                                profilePictureUrl = encodeImageToBase64(imageUri, context)
                            )
                            dbViewModel.updateUser(user) { success ->
                                if (success) {
                                    Toast.makeText(context, "User updated", Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "User update failed",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f),

                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryPurple,
                            contentColor = Color.White,
                        )
                    ) {
                        Text( getString(context, com.tapandtest.app.R.string.update, localeCode = currentLocale))
                    }

                    Button(
                        onClick = {
                            firebaseAuth.signOut()
                            navController.navigate(NavigationItem.LoginScreen.route) {
                                popUpTo(NavigationItem.BaseScreen.route) { inclusive = true }
                            }
                        },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AppColors.PrimaryPurple,
                            contentColor = Color.White,
                        )
                    )

                    {
                        Text( getString(context, com.tapandtest.app.R.string.logout, localeCode = currentLocale))
                    }
                }
            }
        }
    }
}

// Function to encode an image to Base64
fun encodeImageToBase64(uri: Uri?, context: Context): String {
    if (uri == null) return ""
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        if (bitmap == null) return ""
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.DEFAULT)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}

// Function to decode Base64 to Bitmap
fun decodeBase64ToBitmap(base64String: String?): Bitmap? {
    return try {
        if (base64String == null || base64String.isEmpty()) return null
        val decodedString = Base64.decode(base64String, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Function to decode URI to Bitmap
fun decodeUriToBitmap(uri: String?, context: Context): Bitmap? {
    if (uri == null) return null
    return try {
        val inputStream = context.contentResolver.openInputStream(Uri.parse(uri))
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
