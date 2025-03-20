package com.example.tempora.composables.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tempora.R

@Composable
fun SettingsScreen() {
    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.settingsbackground),
            contentDescription = "SettingsScreen Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.8f)
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(Modifier.height(64.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.settings),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(35.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Settings", style = MaterialTheme.typography.titleLarge, color = colorResource(R.color.white), fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(8.dp))

            SettingCard(
                title = "Language",
                icon = painterResource(R.drawable.language),
                options = listOf("English", "Arabic"),
                selectedOption = "English"
            )

            SettingCard(
                title = "Location",
                icon = painterResource(R.drawable.location),
                options = listOf("GPS", "Map"),
                selectedOption = "GPS"
            )

            SettingCard(
                title = "Temperature Unit",
                icon = painterResource(R.drawable.thermostat),
                options = listOf("Kelvin °K", "Celsius °C", "Fahrenheit °F"),
                selectedOption = "Kelvin °K"
            )

            SettingCard(
                title = "Wind Speed Unit",
                icon = painterResource(R.drawable.windspeed),
                options = listOf("Meter/Sec", "Mile/Hour"),
                selectedOption = "Meter/Sec"
            )
        }
    }
}

@Composable
fun SettingCard(title: String, icon: Painter, options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.primaryColor))
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            Row(
                modifier = Modifier.padding(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = icon,
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                options.forEach { option ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onOptionSelected(option) }
                            .padding(vertical = 4.dp)
                    ) {
                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { onOptionSelected(option) },
                            colors = RadioButtonDefaults.colors(selectedColor = colorResource(R.color.white), unselectedColor = colorResource(R.color.white))
                        )
                        Text(text = option, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
