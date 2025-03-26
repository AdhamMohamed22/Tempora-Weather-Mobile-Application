package com.example.tempora.composables.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tempora.R


@Composable
fun City(location: String,country: String = "Default") {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.location_pin),
            contentDescription = null,
            modifier = Modifier.height(24.dp),
            contentScale = ContentScale.FillHeight
        )
        Text(
            text = "$location - $country",
            style = MaterialTheme.typography.titleLarge,
            color = colorResource(R.color.primaryColor),
            fontWeight = FontWeight.Medium
        )
    }
}




