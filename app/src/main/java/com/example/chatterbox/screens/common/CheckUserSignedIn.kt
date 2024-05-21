package com.example.chatterbox.screens.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.navigation.Route

@Composable
fun CheckUserSignedIn(vm: CBViewModel, navController: NavController) {
    val alreadySignedIn = remember {
        mutableStateOf(false)
    }

    val signIn = vm.signIn.value

    if (signIn && !alreadySignedIn.value) {
        alreadySignedIn.value = true
        navController.navigate(Route.ChatListScreen.route) {
            popUpTo(0)
        }
    }

}