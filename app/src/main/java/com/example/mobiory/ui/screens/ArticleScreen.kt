package com.example.mobiory.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArticleScreen(title:String, text:String,bitmap: Bitmap?) {
    Log.e("screen","we here")

    Column(modifier = Modifier.fillMaxSize()) {
           if (bitmap != null) {
               Image(
                   painter = BitmapPainter(bitmap.asImageBitmap()),
                   contentDescription = null,
                   contentScale = ContentScale.FillWidth,
                   modifier = Modifier.fillMaxWidth()
               )
           }else {
               // Placeholder or error image
               Text(
                   text = "Image not available",
                   textAlign = TextAlign.Center,
                   modifier = Modifier
                       .padding(16.dp)
                       .fillMaxWidth()
               )
           }

        Surface(
            color = Color.Black.copy(alpha = 0.6f),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    style = TextStyle(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold, fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
                Text(
                    text = text,
                    color = Color.White,
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp
                )
            }
        }
    }
}


