package com.tapandtest.app.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.fontResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.tapandtest.app.AppColor.AppColors
import com.tapandtest.app.AppNavHost.NavigationItem
import com.tapandtest.app.R

@Composable
fun SplashScreen(navController: NavController)
{

val context = LocalContext.current
val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
val savedTheme = sharedPreferences.getBoolean("dark_mode", false)




Box ( modifier = Modifier.fillMaxSize()


    ) {


    Column ( modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally




    ) {
        Image(
            painterResource(R.drawable.registerscreen),
            contentDescription = "Overlay Logo",
            modifier = Modifier.size(200.dp),





            )

        Text(

            "Testlerinizi Takip Etmenin En Kolay Yolu!\n"+
                    "Defect'leri kaydedin, test görevlerinizi \n" +
                    "yönetin ve notlarınızı ekleyerek süreci daha \nverimli" +
                    " hale getirin.",
            color = if (savedTheme) AppColors.TextDarkPurple else AppColors.PrimaryPurple,
            modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.SemiBold,



        )


        Button(onClick = {
            navController.navigate(NavigationItem.RegisterScreen.route)


        },
            modifier = Modifier.width(250.dp).height(55.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(AppColors.ButtonAccent)


            


        ) {
            Text("Hadi başlayalım!")

        }



    }

}







}

@Composable
@Preview
fun SplashScreenPreview()
{
SplashScreen(rememberNavController() )

}