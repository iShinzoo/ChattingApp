package com.example.chatterbox.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chatterbox.R

@Composable
fun CommonUserInfo(
    imageUrl: String?,
    name: String?,
    onItemClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(start = 12.dp, end = 12.dp, bottom = 8.dp)
            .clickable {
                onItemClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonImage(
            data = imageUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(
                    colorResource(id = R.color.og)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = name ?: "----",
            fontSize = 32.sp,
            color = colorResource(id = R.color.textColor),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp)
        )

    }

}