package com.example.chatterbox.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.R
import com.example.chatterbox.navigation.Route
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ProfileScreen(navController: NavController, vm: CBViewModel) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        ProfileTopAppbar(navController = navController)
        Spacer(modifier = Modifier.height(12.dp))
        ImagePicker()
        Spacer(modifier = Modifier.height(12.dp))
        UserInfoToUpdate()
    }
}

@Composable
fun UserInfoToUpdate() {

    var number by remember { mutableStateOf("") }
    var yourName by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        OutlinedTextField(
            modifier = Modifier.width(380.dp),

            value = yourName,

            onValueChange = { yourName = it },

            placeholder = {
                Text(
                    text = "Enter Name",
                    color = colorResource(id = R.color.textColor)
                )
            },

            singleLine = true,

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

            onValueChange = { number = it },

            placeholder = {
                Text(
                    text = "Enter Number",
                    color = colorResource(id = R.color.textColor)
                )
            },

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
fun ImagePicker() {

    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val chooseProfileImage =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                profileImageUri = uri
            }
        }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = if (profileImageUri != null) rememberImagePainter(profileImageUri) else painterResource(
                id = R.drawable.user
            ),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable {
                    // Open gallery to select image
                    chooseProfileImage.launch("image/*")
                },
            contentScale = ContentScale.Crop
        )
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
                    navController.navigate(Route.ChatListScreen.route)
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
