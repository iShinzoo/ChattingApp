package com.example.chatterbox

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import coil.compose.rememberImagePainter
import com.example.chatterbox.data.CHATS
import com.example.chatterbox.data.ChatData
import com.example.chatterbox.data.ChatUser
import com.example.chatterbox.data.Event
import com.example.chatterbox.data.MESSAGES
import com.example.chatterbox.data.Message
import com.example.chatterbox.data.USER_NODE
import com.example.chatterbox.data.UserData
import com.example.chatterbox.screens.common.ShowToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import java.util.Calendar
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CBViewModel @Inject constructor(
    private val auth: FirebaseAuth, var db: FirebaseFirestore, var storage: FirebaseStorage
) : ViewModel() {


    val inProcess = mutableStateOf(false)
    val inProcessChats = mutableStateOf(false)
    val eventMutbaleState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)
    val chats = mutableStateOf<List<ChatData>>(listOf())
    val chatMessages = mutableStateOf<List<Message>>(listOf())
    val inProcessMessages = mutableStateOf(false)
    var currentChatMessageListener: ListenerRegistration? = null

    init {
        val currentUser = auth.currentUser
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun displayMessages(chatId: String) {
        inProcessMessages.value = true
        currentChatMessageListener = db.collection(CHATS).document(chatId).collection(MESSAGES)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    handleException(error)
                    return@addSnapshotListener
                }
                if (value != null) {
                    chatMessages.value = value.documents.mapNotNull {
                        it.toObject<Message>()
                    }.sortedBy {
                        it.timestamp
                    }
                    inProcessMessages.value = false
                }
            }
    }

    fun hideMessage(){
        chatMessages.value = listOf()
        currentChatMessageListener =  null
    }

    fun displayChats() {
        inProcessChats.value = true
        db.collection(CHATS).where(
            Filter.or(
                Filter.equalTo("user1.userId", userData.value?.userId),
                Filter.equalTo("user2.userId", userData.value?.userId)
            )
        ).addSnapshotListener { value, error ->
            if (error != null) {
                handleException(error)
            }
            if (value != null) {
                chats.value = value.documents.mapNotNull {
                    it.toObject<ChatData>()
                }
                inProcessChats.value = false
            }
        }
    }

    fun SignUp(name: String, number: String, email: String, password: String) {
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all above fields")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number", number).get().addOnSuccessListener {
            if (it.isEmpty) {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener() {
                    if (it.isSuccessful) {
                        signIn.value = true
                        CreateOrUpdateProfile(name, number)
                        // go to home screen
                        Log.d("Tag", "User Logged in!")
                    } else {
                        // handle exception
                        handleException(it.exception, customMessage = "SignUp failed")
                    }
                }
            } else {
                handleException(customMessage = "Number Already Exists")
                inProcess.value = false
            }
        }

    }

    fun Login(email: String, password: String) {
        if (email.isEmpty() or password.isEmpty()) {
            handleException(customMessage = "Please fill all above fields")
            return
        } else {
            inProcess.value = true
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { it ->
                if (it.isSuccessful) {
                    signIn.value = true
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else {
                    handleException(exception = it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun CreateOrUpdateProfile(
        name: String? = null, number: String? = null, imageUrl: String? = null
    ) {
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name ?: userData.value?.name,
            number = number ?: userData.value?.number,
            imageUrl = imageUrl,
        )

        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()) {
                    // update user Data
                    db.collection(USER_NODE).document(uid).update(
                        mapOf(
                            "name" to userData.name,
                            "number" to userData.number,
                            "imageUrl" to imageUrl // Update the imageUrl here
                        )
                    )
                    inProcess.value = false
                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProcess.value = false
                    getUserData(uid)
                }
            }.addOnFailureListener {
                handleException(it, "Cannot Retrieve User")
            }
        }
    }

    private fun getUserData(uid: String) {
        db.collection(USER_NODE).document(uid).addSnapshotListener { value, error ->

            if (error != null) {
                handleException(error, "Cannot Retrieve User")
            }

            if (value != null) {
                val user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
                displayChats()
            }
        }
    }

    fun handleException(exception: Exception? = null, customMessage: String = "") {
        Log.e("Tag", "ChatterBox exception!", exception)
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMessage else customMessage

        eventMutbaleState.value = Event(message)
        inProcess.value = false
    }

    fun uploadProfileImage(uri: Uri) {
        uploadImage(uri) { imageUrl ->
            // After successful upload, update the user's profile with the image URL
            userData.value?.userId?.let { userId ->
                CreateOrUpdateProfile(imageUrl = imageUrl.toString())
            }
        }
    }

    fun uploadImage(uri: Uri, onSuccess: (Uri) -> Unit) {
        inProcess.value = true
        val storageRef = storage.reference
        val uuid = UUID.randomUUID()
        val imageRef = storageRef.child("images/$uuid")
        val uploadTask = imageRef.putFile(uri)
        uploadTask.addOnSuccessListener {

            val result = it.metadata?.reference?.downloadUrl

            result?.addOnSuccessListener(onSuccess)
            inProcess.value = false
        }.addOnFailureListener {
            handleException(it)
        }
    }

    fun Logout() {
        val auth = Firebase.auth
        auth.signOut()
        signIn.value = false
        userData.value = null
        eventMutbaleState.value = Event("Logged Out")
        hideMessage()
        currentChatMessageListener = null
    }


    fun onAddChat(number: String) {
        if (number.isEmpty() || !number.isDigitsOnly()) {
            handleException(customMessage = "Number must contain digits only")
            return
        }

        db.collection(USER_NODE).whereEqualTo("number", number).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    handleException(customMessage = "User with the provided number not found")
                    return@addOnSuccessListener
                }

                val chatPartner = querySnapshot.documents[0].toObject<UserData>()
                val existingChat = chats.value.find { chat ->
                    (chat.user1.number == number && chat.user2.number == userData.value?.number) ||
                            (chat.user1.number == userData.value?.number && chat.user2.number == number)
                }

                if (existingChat != null) {
                    handleException(customMessage = "Chat already exists")
                    return@addOnSuccessListener
                }

                val id = db.collection(CHATS).document().id
                val chat = ChatData(
                    chatId = id,
                    user1 = ChatUser(
                        userData.value?.number,
                        userData.value?.userId,
                        userData.value?.imageUrl,
                        userData.value?.name
                    ),
                    user2 = ChatUser(
                        chatPartner?.number,
                        chatPartner?.userId,
                        chatPartner?.imageUrl,
                        chatPartner?.name
                    )
                )

                db.collection(CHATS).document(id).set(chat).addOnSuccessListener {
                    // Update the chat list in the ViewModel
                    val updatedChats = chats.value.toMutableList()
                    updatedChats.add(chat)
                    chats.value = updatedChats.distinctBy { it.chatId } // Ensure no duplicates
                }.addOnFailureListener { exception ->
                    handleException(exception)
                }
            }.addOnFailureListener { exception ->
                handleException(exception)
            }
    }

    fun onSendReply(chatId: String, message: String) {
        val time = Calendar.getInstance().time.toString()
        val msg = Message(userData.value?.userId, message, time)
        db.collection(CHATS).document(chatId).collection(MESSAGES).document().set(msg)
    }


}

