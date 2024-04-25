package com.example.chatterbox.data

open class Event<out T>(val content : T) {
    var hasBeenHandeled = false
    fun getContentorNull() : T? {
        return if (hasBeenHandeled) null
        else{
            hasBeenHandeled = true
            content
        }
    }
}