package com.example.chatterbox.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.screens.ChatListScreen
import com.example.chatterbox.screens.LoginScreen
import com.example.chatterbox.screens.ProfileScreen
import com.example.chatterbox.screens.SignUpScreen
import com.example.chatterbox.screens.SingleChatScreen
import com.example.chatterbox.screens.WelcomeScreen
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun NavGraph(vm : CBViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Route.WelcomeScreen.route) {

        composable(
            route = Route.WelcomeScreen.route
        ) {
            WelcomeScreen(navController = navController)
        }

        composable(
            route = Route.LoginScreen.route
        ) {
            LoginScreen(navController = navController, vm = vm)
        }

        composable(
            route = Route.SignUpScreen.route
        ) {
            SignUpScreen(navController = navController, vm = vm)
        }

        composable(
            route = Route.ChatListScreen.route
        ) {
            ChatListScreen(navController = navController)
        }

        composable(
            route = Route.SingleChatScreen.route
        ) {
            SingleChatScreen(navController = navController)
        }
        composable(
            route = Route.ProfileScreen.route
        ) {
            ProfileScreen(navController = navController)
        }
    }
}