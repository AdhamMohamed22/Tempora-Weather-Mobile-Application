package com.example.tempora.composables.home.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempora.R


@Preview
@Composable
fun TemperatureDegree(degree: String = "21°C") {
    Text(
        text = degree,
        letterSpacing = 0.sp,
        style = TextStyle(
            brush = Brush.verticalGradient(
                0f to colorResource(R.color.primaryColor),
                1f to colorResource(R.color.primaryColor).copy(alpha = 0.3f)
            ),
            fontSize = 70.sp,
            fontWeight = FontWeight.Black
        ),
    )
}
