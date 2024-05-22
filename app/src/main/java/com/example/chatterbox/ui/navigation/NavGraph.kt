package com.example.chatterbox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.ui.screen.ChatListScreen
import com.example.chatterbox.ui.screen.LoginScreen
import com.example.chatterbox.ui.screen.ProfileScreen
import com.example.chatterbox.ui.screen.SignUpScreen
import com.example.chatterbox.ui.screen.SingleChatScreen
import com.example.chatterbox.ui.screen.WelcomeScreen

@Composable
fun NavGraph(vm: CBViewModel) {
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
            ChatListScreen(navController = navController, vm = vm)
        }

        composable(
            route = Route.SingleChatScreen.route
        ) {
            val chatId = it.arguments?.getString("chatId")
            chatId?.let {
                SingleChatScreen(navController = navController, vm = vm, chatId = chatId)
            }
        }

        composable(
            route = Route.ProfileScreen.route
        ) {
            ProfileScreen(navController = navController, vm = vm)
        }
    }
}