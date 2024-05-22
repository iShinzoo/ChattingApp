package com.example.chatterbox.ui.screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.R
import com.example.chatterbox.ui.navigation.Route
import com.example.chatterbox.ui.screen.common.CommonImage

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

    val context = LocalContext.current

    ProfileContent(
        navController = navController,
        imageUrl = imageUrl,
        vm = vm,
        onSave = {
            vm.CreateOrUpdateProfile(
                name = name,
                number = number,
                imageUrl = imageUrl
            )
            Toast.makeText(context, "Details Updated Successfully", Toast.LENGTH_SHORT).show()
        },
        onLogout = {
            vm.Logout()
            navController.navigate(Route.LoginScreen.route)
        },
        name = name,
        number = number,
        onNameChange = { name = it },
        onNumberChange = { number = it }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    navController: NavController,
    imageUrl: String?,
    vm: CBViewModel,
    onSave: () -> Unit,
    onLogout: () -> Unit,
    name: String,
    number: String,
    onNameChange: (String) -> Unit,
    onNumberChange: (String) -> Unit
) {

    val context = LocalContext.current

    var isEnabled by remember { mutableStateOf(false) }
    var isEnabledo by remember { mutableStateOf(false) }

    // Top App Bar

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {

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
                        onSave()
                    }) {
                        Icon(
                            Icons.Default.SaveAs,
                            contentDescription = "Localized description",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = {
                        onLogout()
                        Toast.makeText(context, "Logout Successfully", Toast.LENGTH_SHORT).show()
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
        Spacer(modifier = Modifier.height(18.dp))

        // Image Picker Section

        val chooseProfileImage =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                uri?.let {
                    vm.uploadProfileImage(uri)
                }
            }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            CommonImage(data = imageUrl,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
                    .clickable {
                        // Open gallery to select image
                        chooseProfileImage.launch("image/*")
                    }
                    .clip(CircleShape)
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
        Spacer(modifier = Modifier.height(18.dp))

        // user information updating section

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                modifier = Modifier.width(380.dp),

                value = name,

                onValueChange = onNameChange,

                enabled = isEnabled,

                singleLine = true,

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(id = R.color.textColor),
                    focusedBorderColor = colorResource(id = R.color.textColor),
                    cursorColor = colorResource(id = R.color.textColor),
                    disabledBorderColor = colorResource(id = R.color.textColor),
                    disabledTextColor = colorResource(id = R.color.textColor),
                    disabledLeadingIconColor = colorResource(id = R.color.textColor),
                    disabledTrailingIconColor = colorResource(id = R.color.textColor)
                ),

                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null)
                },

                trailingIcon = {
                    IconButton(onClick = {
                        isEnabled = !isEnabled
                    }) {

                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")

                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                modifier = Modifier.width(380.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = colorResource(id = R.color.textColor),
                    focusedBorderColor = colorResource(id = R.color.textColor),
                    cursorColor = colorResource(id = R.color.textColor),
                    disabledBorderColor = colorResource(id = R.color.textColor),
                    disabledTextColor = colorResource(id = R.color.textColor),
                    disabledLeadingIconColor = colorResource(id = R.color.textColor),
                    disabledTrailingIconColor = colorResource(id = R.color.textColor)
                ),

                value = number,

                enabled = isEnabledo,

                onValueChange = onNumberChange,

                singleLine = true,

                leadingIcon = {
                    Icon(Icons.Default.Phone, contentDescription = null)
                },

                trailingIcon = {
                    IconButton(onClick = {
                        isEnabledo = !isEnabledo
                    }) {

                        Icon(imageVector = Icons.Default.Edit, contentDescription = "")

                    }
                }
            )
        }
    }
}





