package com.example.tempora.composables.home.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tempora.R

@Composable
fun WeatherDescription(description: String) {
    Text(
        text = description,
        fontSize = 22.sp,
        color = colorResource(R.color.primaryColor),
        fontWeight = FontWeight.Thin
    )
}