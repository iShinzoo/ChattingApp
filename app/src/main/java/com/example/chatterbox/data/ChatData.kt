package com.example.chatterbox.data

data class ChatData(
    val chatId : String?= "",
    val user1 : ChatUser = ChatUser(),
    val user2 : ChatUser = ChatUser()
)
