


package com.tapandtest.app.Screens

import TimerViewModel
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.R
import kotlinx.coroutines.delay

@SuppressLint("StringFormatInvalid")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BaseScreen(navController: NavController, timerViewModel: TimerViewModel) {
    val sharedPreferences = LocalContext.current.getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    var currentLocale = sharedPreferences.getString("language", "") ?: ""



    val context = LocalContext.current
    val totalOptions = listOf(1, 2, 5, 10, 15, 20, 30, 60, 90, 120, 150, 180, 240, 300)

    val backgroundGradient = listOf(Color(0xFFEDE7F6), Color(0xFFF3E5F5))
    val White = Color.White

    var expanded by remember { mutableStateOf(false) }

    val selectedMinutes by remember { timerViewModel.selectedMinutes }
    val timeLeft by remember { timerViewModel.timeLeft }
    val isRunning by remember { timerViewModel.isRunning }

    val progress = timeLeft.toFloat() / (selectedMinutes * 60 * 1000)

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    1001
                )
            }
        }
    }

    // Timer'ın çalışmasını yönetmek
    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0 && isRunning) {
                delay(1000L)
                timerViewModel.updateTime()
            }
            if (timeLeft <= 0) {
                sendNotification(
                    context,
                    context.getString(R.string.EndTimer),
                    context.getString(R.string.ContrasyonSelect)
                )
                timerViewModel.resetTimer()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()

    ) {
        Text(
            text = getString(context,R.string.ContrasyonSelect, localeCode = currentLocale),
            color = AppColors.PrimaryAmethyst,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()


                .clickable { expanded = true }

                .padding(horizontal = 16.dp, vertical = 12.dp)

        ) {
            Text(
                text = getString(context, R.string.Minutes, localeCode = currentLocale) + ": $selectedMinutes ${getString(context, R.string.Second, localeCode = currentLocale)}",
                color = AppColors.PrimaryAmethyst,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.padding(12.dp))


        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            totalOptions.forEach { minute ->
                DropdownMenuItem(
                    text = {
                        Text("$minute ${getString(context, R.string.Second, localeCode = currentLocale)}")
                    },
                    onClick = {
                        timerViewModel.setMinutes(minute)
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),

            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
            ) {
                Text(
                    text = getString(context, R.string.Minutes, localeCode = currentLocale) + ": ${timeLeft / 1000} ${getString(context, R.string.Second, localeCode = currentLocale)}",



                    color = AppColors.PrimaryAmethyst,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                LinearProgressIndicator(
                    progress = progress,
                    color = AppColors.PrimaryPurple,
                    trackColor = (AppColors.TextDarkSecondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .background(color = AppColors.TextDarkSecondary, RoundedCornerShape(5.dp))
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { timerViewModel.toggleTimer() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp), colors= ButtonDefaults.buttonColors(
                        containerColor = AppColors.PrimaryAmethyst,
                        contentColor = if (isSystemInDarkTheme()) Color.White else Color.Black,
                        disabledContainerColor = AppColors.TextDarkGray,
                        disabledContentColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    )

                ) {
                    Text(
                        text = if (isRunning)
                            getString(context, R.string.StopTimer, localeCode = currentLocale)
                        else
                            getString(context, R.string.StartTimer, localeCode = currentLocale),
                        style = MaterialTheme.typography.bodyLarge


                    )
                }
            }
        }
    }
}



fun sendNotification(context: Context, title: String, message: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "timer_notification_channel"
        val channelName = "Timer Notifications"
        val channelDescription = "Notifications for timer"

        val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(context, "timer_notification_channel")
        .setContentTitle(title)
        .setContentText(message)
        .setSmallIcon(android.R.drawable.ic_notification_overlay)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .build()

    notificationManager.notify(1, notification)
}

