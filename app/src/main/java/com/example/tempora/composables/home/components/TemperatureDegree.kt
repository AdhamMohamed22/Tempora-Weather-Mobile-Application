package com.example.tempora.composables.home.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tempora.R
import com.example.tempora.utils.helpers.formatNumberBasedOnLanguage
import com.example.tempora.utils.helpers.formatTemperatureUnitBasedOnLanguage


@Composable
fun TemperatureDegree(degree: String, unit: String) {
    Text(
        text = formatNumberBasedOnLanguage("$degree ${formatTemperatureUnitBasedOnLanguage(unit)}"),
        letterSpacing = 0.sp,
        style = TextStyle(
            brush = Brush.verticalGradient(
                0f to colorResource(R.color.primaryColor),
                1f to colorResource(R.color.primaryColor).copy(alpha = 0.3f)
            ),
            fontSize = 60.sp,
            fontWeight = FontWeight.Black
        ),
    )
}
