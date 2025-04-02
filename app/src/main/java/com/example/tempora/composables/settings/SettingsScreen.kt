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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.settings.utils.SharedPref
import com.example.tempora.composables.settings.utils.getLanguage
import com.example.tempora.composables.settings.utils.getLanguageValue
import com.example.tempora.composables.settings.utils.getLocation
import com.example.tempora.composables.settings.utils.getLocationValue
import com.example.tempora.composables.settings.utils.getTemperatureUnit
import com.example.tempora.composables.settings.utils.getTemperatureValue
import com.example.tempora.composables.settings.utils.getWindSpeedUnit
import com.example.tempora.composables.settings.utils.getWindSpeedValue

@Composable
fun SettingsScreen(showFAB: MutableState<Boolean>, navigationAction: () -> Unit) {
    showFAB.value = false

    val context = LocalContext.current
    val settingsScreenViewModelFactory =
        SettingsScreenViewModel.SettingScreenViewModelFactory(PreferencesManager.getInstance(context))
    val viewModel: SettingsScreenViewModel = viewModel(factory = settingsScreenViewModelFactory)

    val selectedLanguageState by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedLocationState by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val selectedTemperatureUnitState by viewModel.selectedTemperatureUnit.collectAsStateWithLifecycle()
    val selectedWindSpeedUnitState by viewModel.selectedWindSpeedUnit.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize())
    {
        Image(
            painter = painterResource(id = R.drawable.settingsbackground),
            contentDescription = "SettingsScreen Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.8f)
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
                Text(
                    stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleLarge,
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(16.dp))

            SettingCard(
                title = stringResource(R.string.language),
                icon = painterResource(R.drawable.language),
                options = listOf(
                    stringResource(R.string.english),
                    stringResource(R.string.arabic),
                    stringResource(R.string.default_lang)
                ),
                selectedOption = getLanguage(selectedLanguageState),
                onOptionSelected = {
                    viewModel.savePreference(
                        PreferencesManager.LANGUAGE_KEY,
                        getLanguageValue(it),
                        context
                    )
                }
            )

            SettingCard(
                title = stringResource(R.string.location),
                icon = painterResource(R.drawable.location),
                options = listOf(stringResource(R.string.gps), stringResource(R.string.map)),
                selectedOption = getLocation(selectedLocationState),
                onOptionSelected = {
                    viewModel.savePreference(
                        PreferencesManager.LOCATION_KEY,
                        getLocationValue(it),
                        context
                    )
                    if (it == "Map" || it == "الخريطة") {
                        navigationAction()
                    } else {
                        val sharedPref = SharedPref.getInstance(context)
                        sharedPref.setGpsSelected(true)
                    }
                }
            )

            SettingCard(
                title = stringResource(R.string.temperature_unit),
                icon = painterResource(R.drawable.thermostat),
                options = listOf(
                    stringResource(R.string.kelvin_k),
                    stringResource(R.string.celsius_c), stringResource(R.string.fahrenheit_f)
                ),
                selectedOption = getTemperatureUnit(selectedTemperatureUnitState),
                onOptionSelected = {
                    viewModel.savePreference(
                        PreferencesManager.TEMPERATURE_UNIT_KEY,
                        getTemperatureValue(it),
                        context
                    )
                }
            )

            SettingCard(
                title = stringResource(R.string.wind_speed_unit),
                icon = painterResource(R.drawable.windspeed),
                options = listOf(
                    stringResource(R.string.meter_sec),
                    stringResource(R.string.mile_hour)
                ),
                selectedOption = getWindSpeedUnit(selectedWindSpeedUnitState),
                onOptionSelected = {
                    viewModel.savePreference(
                        PreferencesManager.WIND_SPEED_UNIT_KEY,
                        getWindSpeedValue(it),
                        context
                    )
                }
            )
        }
    }
}

@Composable
fun SettingCard(
    title: String,
    icon: Painter,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
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
                            colors = RadioButtonDefaults.colors(
                                selectedColor = colorResource(R.color.white),
                                unselectedColor = colorResource(R.color.white)
                            )
                        )
                        Text(
                            text = option,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
