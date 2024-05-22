package com.example.chatterbox.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chatterbox.CBViewModel
import com.example.chatterbox.R
import com.example.chatterbox.data.Message
import com.example.chatterbox.ui.navigation.Route
import com.example.chatterbox.ui.screen.common.CommonImage

@Composable
fun SingleChatScreen(navController: NavController, vm: CBViewModel, chatId: String) {

    var reply by rememberSaveable {
        mutableStateOf("")
    }

    val onSendReply = {
        vm.onSendReply(chatId, reply)
        reply = ""
    }

    val myUser = vm.userData.value

    val currentChat = vm.chats.value.first { it.chatId == chatId }

    val chatUser =
        if (myUser?.userId == currentChat.user1.userId) currentChat.user2 else currentChat.user1

    val chatMsg by vm.chatMessages

    LaunchedEffect(key1 = chatId) {
        vm.displayMessages(chatId)
    }

    BackHandler {
        navController.navigate(Route.ChatListScreen.route)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(Color.White)
    ) {

        TopChatBar(name = chatUser.name ?: "", imageUrl = chatUser.imageUrl ?: "") {
            navController.popBackStack()
            vm.hideMessage()
        }
        MessageBox(
            modifier = Modifier.weight(1f),
            chatMessage = chatMsg,
            currentUserId = myUser?.userId ?: ""
        )
        ReplyBox(reply = reply, onReplyChange = { reply = it }, onSendReply = onSendReply)
    }
}


@Composable
fun MessageBox(modifier: Modifier, chatMessage: List<Message>, currentUserId: String) {
    val listState = rememberLazyListState()

    LaunchedEffect(chatMessage) {
        if (chatMessage.isNotEmpty()) {
            listState.animateScrollToItem(chatMessage.size - 1)
        }
    }

    LazyColumn(modifier = modifier, state = listState) {
        items(chatMessage) { msg ->
            val alignment = if (msg.sendBy == currentUserId) Alignment.End else Alignment.Start
            val color =
                if (msg.sendBy == currentUserId) colorResource(id = R.color.og) else colorResource(
                    id = R.color.textColor
                )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = alignment
            ) {
                Text(
                    text = msg.message ?: "",
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(12.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopChatBar(name: String, imageUrl: String, onBackClicked: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBackIosNew, contentDescription = null,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable {
                            onBackClicked.invoke()
                        },
                    tint = colorResource(id = R.color.white)
                )
                Spacer(modifier = Modifier.width(22.dp))
                CommonImage(
                    data = imageUrl,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            colorResource(id = R.color.og)
                        )
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = name,
                    color = colorResource(id = R.color.white),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        },
        actions = {
            IconButton(onClick = {

            }) {
                Icon(
                    Icons.Default.Phone,
                    contentDescription = "Localized description",
                    tint = Color.White
                )
            }
            IconButton(onClick = {

            }) {
                Icon(
                    Icons.Default.VideoCall,
                    contentDescription = "Localized description",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = colorResource(R.color.og))
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReplyBox(
    reply: String,
    onReplyChange: (String) -> Unit,
    onSendReply: () -> Unit
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = reply,
                onValueChange = onReplyChange,
                placeholder = {
                    Text(text = "Type something .....")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp)
                    .senderBarBorder(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = colorResource(id = R.color.white),
                    focusedTextColor = Color.Black,
                    cursorColor = Color.Black,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    IconButton(onClick = onSendReply) {
                        Icon(
                            Icons.Default.Send,
                            contentDescription = null,
                            tint = colorResource(id = (R.color.og)),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                },
                leadingIcon = {
                    IconButton(onClick = {
                    }) {
                        Icon(
                            Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = colorResource(id = (R.color.og)),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    }
}


fun Modifier.senderBarBorder() = composed {
    if (!isSystemInDarkTheme() || isSystemInDarkTheme()) {
        border(
            width = 1.dp,
            color = colorResource(id = (R.color.og)),
            shape = MaterialTheme.shapes.medium
        )
    } else {
        this
    }
}
