package com.example.chatterbox.ui.screen

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.R
import com.example.chatterbox.ui.navigation.Route
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun WelcomeScreen(navController: NavController) {
    // when the user is logged in
    val auth = Firebase.auth
    if (auth.currentUser != null) {
        navController.navigate(Route.ChatListScreen.route)
    }

    BackHandler(true) {
        (navController.context as ComponentActivity).finish()
    }

    // this screen will be shown to user when he/she is logged out
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = " Welcome to ChatterBox",
                modifier = Modifier.padding(top = 120.dp),
                color = colorResource(id = R.color.textColor),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(40.dp))



            Image(
                painter = painterResource(id = R.drawable.chat),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 60.dp),
                contentScale = ContentScale.Fit
            )


            Spacer(modifier = Modifier.height(40.dp))

            ElevatedButton(
                onClick = {
                    navController.navigate(route = Route.LoginScreen.route)
                },
                modifier = Modifier
                    .width(400.dp)
                    .padding(start = 30.dp, end = 30.dp),
                colors = ButtonDefaults.elevatedButtonColors(colorResource(id = R.color.textColor))
            ) {
                Text(
                    text = "Login",
                    modifier = Modifier,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = {
                    navController.navigate(route = Route.SignUpScreen.route)
                },
                modifier = Modifier
                    .width(400.dp)
                    .padding(start = 30.dp, end = 30.dp),
                border = BorderStroke(width = 2.dp, color = colorResource(id = R.color.textColor))
            ) {
                Text(
                    text = "Don't have an Account? SignUP",
                    modifier = Modifier,
                    color = colorResource(id = R.color.textColor),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }

    }
}