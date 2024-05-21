package com.example.chatterbox.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatterbox.R


@Composable
fun MediaOptionsBottomSheet(onOptionSelected: (MediaOption) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(colorResource(id = R.color.white))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select an option", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onOptionSelected(MediaOption.Photo) }) {
                Icon(
                    imageVector = Icons.Default.Photo,
                    contentDescription = "Photo",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
            IconButton(onClick = { onOptionSelected(MediaOption.Gif) }) {
                Icon(
                    imageVector = Icons.Default.Gif,
                    contentDescription = "GIF",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
            IconButton(onClick = { onOptionSelected(MediaOption.Document) }) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Document",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
            IconButton(onClick = { onOptionSelected(MediaOption.Audio) }) {
                Icon(
                    imageVector = Icons.Default.Audiotrack,
                    contentDescription = "Audio",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onOptionSelected(MediaOption.Camera) }) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
            IconButton(onClick = { onOptionSelected(MediaOption.Contact) }) {
                Icon(
                    imageVector = Icons.Default.Contacts,
                    contentDescription = "Contact",
                    modifier = Modifier.size(32.dp),
                    tint = colorResource(id = R.color.og)
                )
            }
        }
    }
}

enum class MediaOption {
    Photo, Gif, Document, Audio, Camera, Contact
}
