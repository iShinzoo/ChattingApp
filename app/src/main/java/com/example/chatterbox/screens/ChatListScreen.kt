package com.example.chatterbox.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.rounded.AddComment
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.R
import com.example.chatterbox.navigation.Route
import com.example.chatterbox.screens.common.ProgressBar

@Composable
fun ChatListScreen(navController: NavController, vm: CBViewModel) {

    BackHandler(true) {
        // Close the app when back button is pressed
        (navController.context as ComponentActivity).finish()
    }

    val inProcess = vm.inProcessChats
    if (inProcess.value) {
        ProgressBar()
    } else {
        val chats = vm.chats.value
        val userData = vm.userData.value
        val showDialog = remember {
            mutableStateOf(false)
        }
        val onFabClick: () -> Unit = { showDialog.value = true }
        val onDismiss: () -> Unit = { showDialog.value = false }
        val onAddChat: (String) -> Unit = {
            vm.onAddChat(it)
            showDialog.value = false
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            TopAppbar(navController = navController)

            Scaffold(
                floatingActionButton = {
                    Fab(
                        showDialog = showDialog.value,
                        onDismiss = onDismiss,
                        onFabClick = onFabClick,
                        onAddChat = onAddChat)
                },
                content = {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ){
                        if (chats.isEmpty()){
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "No Chats Available")
                            }
                        }
                    }
                }
            )
        }
    }

}


@Composable
fun Fab(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onFabClick: () -> Unit,
    onAddChat: (String) -> Unit
) {
    val addChatNumber = remember {
        mutableStateOf("")
    }

    if (showDialog) {
        AlertDialog(onDismissRequest = { onDismiss.invoke() }, confirmButton = {
            Button(onClick = { onAddChat(addChatNumber.value) },
                colors = ButtonDefaults.buttonColors(colorResource(id = R.color.textColor))) {
                Text(text = "Add Chat")
            }
        },
            title = { Text(text = "Add Chat") },
            text = {
                OutlinedTextField(
                    value = addChatNumber.value, onValueChange = { addChatNumber.value = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(id = R.color.textColor),
                        focusedBorderColor = colorResource(id = R.color.textColor),
                        cursorColor = colorResource(id = R.color.textColor)
                    )
                )
            }
        )
    }
    FloatingActionButton(
        onClick = { onFabClick() },
        containerColor = colorResource(id = R.color.og),
        shape = CircleShape,
        modifier = Modifier.padding(bottom = 40.dp)
    ) {

        Icon(
            imageVector = Icons.Rounded.AddComment,
            contentDescription = null,
            tint = colorResource(
                id = R.color.white
            )
        )
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppbar(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "ChatterBox",
                    color = colorResource(id = R.color.white),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            actions = {
                IconButton(onClick = {
                    navController.navigate(Route.ProfileScreen.route)
                }) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(R.color.og))
        )
    }
}