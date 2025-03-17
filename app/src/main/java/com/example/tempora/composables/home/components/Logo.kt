package com.example.tempora.composables.home.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tempora.R

@Preview
@Composable
fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.tempora_transparent),
        contentDescription = "Icon Weather Status",
        contentScale = ContentScale.Crop
    )
}