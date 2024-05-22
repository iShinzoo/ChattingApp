package com.example.chatterbox.ui.navigation

sealed class Route (
    val route : String
){
    data object LoginScreen : Route(route = "loginScreen")

    data object SignUpScreen : Route(route = "signupScreen")

    data object ChatListScreen : Route(route = "chatListScreen")

    data object WelcomeScreen : Route(route = "welcomeScreen")

    data object SingleChatScreen : Route(route = "singleChatScreen/{chatId}"){
        fun createRoute(id : String) = "singleChatScreen/$id"
    }

    data object ProfileScreen : Route(route = "profileScreen")
}