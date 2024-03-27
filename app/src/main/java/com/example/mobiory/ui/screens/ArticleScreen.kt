package com.example.mobiory.ui.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobiory.ui.viewModel.ArticleViewModel

@Composable
fun ArticleScreen() {

    val viewModel: ArticleViewModel = hiltViewModel<ArticleViewModel>()

    val title = viewModel.articleTitle.value
    val text = viewModel.articleText.value
    val bitmap = viewModel.articleImageBitmap.value?.asImageBitmap()
    Column(modifier = Modifier.fillMaxSize()) {
        if (bitmap != null) {
            Image(
                painter = BitmapPainter(bitmap),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        } else {
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
                if (title != null) {
                    Text(
                        text = title,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
                if (text != null) {
                    Text(
                        text = text,
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


