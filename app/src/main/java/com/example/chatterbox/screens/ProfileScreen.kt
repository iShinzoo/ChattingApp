package com.example.chatterbox.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SaveAs
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.R
import com.example.chatterbox.navigation.Route
import com.example.chatterbox.screens.common.CommonImage
import com.example.chatterbox.screens.common.ProgressBar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController, vm: CBViewModel) {

    val userData = vm.userData.value

    var name by rememberSaveable {
        mutableStateOf(userData?.name ?: "")
    }
    var number by rememberSaveable {
        mutableStateOf(userData?.number ?: "")
    }

    val imageUrl = vm.userData.value?.imageUrl
    val inProgress = vm.inProcess.value
    if (inProgress) {
        ProgressBar()
    } else {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProfileTopAppbar(navController = navController)
            Spacer(modifier = Modifier.height(12.dp))
            ImagePicker(imageUrl = imageUrl, vm = vm)
            Spacer(modifier = Modifier.height(12.dp))
            UserInfoToUpdate(
                name = name,
                number = number,
                onNameChange = { name = it },
                onNumberChange = { number = it }
            )
        }
    }
}

@Composable
fun UserInfoToUpdate(
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            modifier = Modifier.width(380.dp),

            value = name,

            onValueChange = onNameChange,

            singleLine = true,

            colors = TextFieldDefaults.colors(
                focusedTextColor = colorResource(id = R.color.textColor),
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),

            leadingIcon = {
                Icon(Icons.Default.Person, contentDescription = null)
            },

            trailingIcon = {
                IconButton(onClick = {}) {

                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")

                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            modifier = Modifier.width(380.dp),

            value = number,

            onValueChange = onNumberChange,

            singleLine = true,

            leadingIcon = {
                Icon(Icons.Default.Phone, contentDescription = null)
            },

            trailingIcon = {
                IconButton(onClick = {}) {

                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")

                }
            }
        )
    }
}


@Composable
fun ImagePicker(imageUrl: String?, vm: CBViewModel) {


    val chooseProfileImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                vm.uploadProfileImage(uri)
            }
        }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = CircleShape,
                modifier = Modifier
                    .padding(8.dp)
                    .size(100.dp)
                    .clickable {
                        // Open gallery to select image
                        chooseProfileImage.launch("image/*")
                    }
            ) {
                CommonImage(data = imageUrl)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Change Profile Picture",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.W500,
                color = colorResource(id = R.color.textColor)
            )
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileTopAppbar(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        TopAppBar(
            title = {
                IconButton(onClick = {
                    navController.navigate(Route.ChatListScreen.route)
                }) {
                    Icon(
                        Icons.Default.ArrowBackIosNew,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            },
            actions = {
                IconButton(onClick = {

                }) {
                    Icon(
                        Icons.Default.SaveAs,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
                IconButton(onClick = {
                    val auth = Firebase.auth
                    auth.signOut()
                    navController.navigate(Route.LoginScreen.route)
                }) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = "Localized description",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(R.color.og))
        )
    }
}
