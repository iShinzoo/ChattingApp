package com.example.chatterbox

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.chatterbox.data.Event
import com.example.chatterbox.data.USER_NODE
import com.example.chatterbox.data.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class CBViewModel @Inject constructor(
    private val auth : FirebaseAuth,
    var db : FirebaseFirestore
): ViewModel() {


    val inProcess = mutableStateOf(false)
    val eventMutbaleState = mutableStateOf<Event<String>?>(null)
    var signIn = mutableStateOf(false)
    val userData = mutableStateOf<UserData?>(null)

    init {
        val currentUser = auth.currentUser
        currentUser?.uid?.let {
            getUserData(it)
        }
    }

    fun SignUp(name : String,number : String,email : String,password: String){
        inProcess.value = true
        if (name.isEmpty() or number.isEmpty() or email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please fill all above fields")
            return
        }
        inProcess.value = true
        db.collection(USER_NODE).whereEqualTo("number",number).get().addOnSuccessListener {
            if (it.isEmpty){
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener() {
                    if (it.isSuccessful){
                        signIn.value = true
                        CreateOrUpdateProfile(name,number)
                        // go to home screen
                        Log.d("Tag","User Logged in!")
                    } else{
                        // handle exception
                        handleException(it.exception, customMessage = "SignUp failed")
                    }
                }
            }
            else{
              handleException(customMessage = "Number Already Exists")
                inProcess.value = false
            }
        }

    }

    fun Login(email: String, password: String){
        if (email.isEmpty() or password.isEmpty()){
            handleException(customMessage = "Please fill all above fields")
            return
        } else{
            inProcess.value = true
            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{ it ->
                if (it.isSuccessful){
                    signIn.value = true
                    auth.currentUser?.uid?.let {
                        getUserData(it)
                    }
                } else{
                    handleException(exception = it.exception, customMessage = "Login Failed")
                }
            }
        }
    }

    fun CreateOrUpdateProfile(name : String?= null,number : String?= null,imageUrl : String?= null){
        val uid = auth.currentUser?.uid
        val userData = UserData(
            userId = uid,
            name = name?: userData.value?.name,
            number = number?: userData.value?.number,
            imageUrl = imageUrl?: userData.value?.imageUrl,
        )

        uid?.let {
            inProcess.value = true
            db.collection(USER_NODE).document(uid).get().addOnSuccessListener {
                if (it.exists()){
                    // update user Data
                } else {
                    db.collection(USER_NODE).document(uid).set(userData)
                    inProcess.value = false
                    getUserData(uid)
                }
            }
                .addOnFailureListener{
                    handleException(it,"Cannot Retrieve User")
                }
        }
    }

    private fun getUserData(uid: String) {
        db.collection(USER_NODE).document(uid).addSnapshotListener{
            value , error ->

            if (error != null){
                handleException(error,"Cannot Retrieve User")
            }

            if (value != null){
                var user = value.toObject<UserData>()
                userData.value = user
                inProcess.value = false
            }
        }
    }

    fun handleException(exception : Exception?=null, customMessage : String = ""){
        Log.e("Tag","ChatterBox exception!",exception)
        exception?.printStackTrace()
        val errorMessage = exception?.localizedMessage?:""
        val message = if (customMessage.isNullOrEmpty())errorMessage else customMessage

        eventMutbaleState.value = Event(message)
        inProcess.value = false
    }

}