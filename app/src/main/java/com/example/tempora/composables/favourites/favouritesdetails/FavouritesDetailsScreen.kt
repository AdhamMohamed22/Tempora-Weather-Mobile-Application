package com.example.tempora.composables.favourites.favouritesdetails

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.composables.home.components.DisplayCurrentWeather
import com.example.tempora.composables.home.components.ListOf5WeekDaysCards
import com.example.tempora.composables.home.components.ListOfHourCards
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.models.Item0
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.CurrentWeatherResponseState
import com.example.tempora.data.response_state.ForecastWeatherResponseState
import com.example.tempora.composables.home.components.LoadingIndicator
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FavouritesDetailsScreen(showFAB: MutableState<Boolean>, favouriteLocation: FavouriteLocation) {

    showFAB.value = false
    val context = LocalContext.current

    val favouritesDetailsViewModelFactory =
        FavouritesDetailsScreenViewModel.FavouritesDetailsViewModelFactory(
            Repository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.retrofit),
                WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao())
            )
        )
    val viewModel: FavouritesDetailsScreenViewModel = viewModel(factory = favouritesDetailsViewModelFactory)

    val currentWeatherState by viewModel.currentWeather.collectAsStateWithLifecycle()
    val todayForecastWeatherState by viewModel.todayForecastWeather.collectAsStateWithLifecycle()
    val daysForecastWeather by viewModel.daysForecastWeather.collectAsStateWithLifecycle()
    val selectedUnit by viewModel.selectedUnit.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.message.collect{
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getCurrentWeather(favouriteLocation.latitude,favouriteLocation.longitude,context)
        viewModel.getTodayForecastWeather(favouriteLocation.latitude,favouriteLocation.longitude,context)
        viewModel.get5DaysForecastWeather(favouriteLocation.latitude,favouriteLocation.longitude,context)
    }

    Box(modifier = Modifier.fillMaxSize())
    {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
            contentDescription = "HomeScreen Background",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(alpha = 0.8f)
        )
        // Foreground UI elements
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (currentWeatherState){
                is CurrentWeatherResponseState.Loading -> LoadingIndicator()
                is CurrentWeatherResponseState.Failed -> { DisplayCurrentWeather(favouriteLocation.currentWeather,selectedUnit) }
                is CurrentWeatherResponseState.Success -> { val currentWeather = (currentWeatherState as CurrentWeatherResponseState.Success).currentWeather
                    DisplayCurrentWeather(currentWeather, selectedUnit) }
            }

            when(todayForecastWeatherState){
                is ForecastWeatherResponseState.Loading -> LoadingIndicator()
                is ForecastWeatherResponseState.Failed -> { ListOfHourCards(favouriteLocation.forecastWeather.list.subList(0,8),selectedUnit) }
                is ForecastWeatherResponseState.Success -> { val todayForecast = (todayForecastWeatherState as ForecastWeatherResponseState.Success).forecastWeather
                    ListOfHourCards(todayForecast,selectedUnit) }
            }

            when(daysForecastWeather){
                is ForecastWeatherResponseState.Loading -> LoadingIndicator()
                is ForecastWeatherResponseState.Failed -> { Display5DaysForecastOffline(favouriteLocation.forecastWeather.list,selectedUnit) }
                is ForecastWeatherResponseState.Success -> { val fiveDaysList = (daysForecastWeather as ForecastWeatherResponseState.Success).forecastWeather
                    ListOf5WeekDaysCards(fiveDaysList,selectedUnit) }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Display5DaysForecastOffline(daysList: List<Item0>, selectedUnit: String) {
    val today = LocalDate.now(ZoneId.of("Africa/Cairo"))
    val todayFormattedDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

    val coroutineScope = rememberCoroutineScope()
    var filteredList by remember { mutableStateOf<List<Item0>>(emptyList()) }

    LaunchedEffect(daysList) {  // Automatically triggers when daysList changes
        coroutineScope.launch {
            val result = daysList
                .filter { item -> item.dt_txt.substringBefore(" ") != todayFormattedDate } // Exclude today's forecast
                .groupBy { item -> item.dt_txt.substringBefore(" ") } // Group by date
                .map { (_, items) -> items.first() } // Take the first item from each date

            filteredList = result // Update state
        }
    }

    ListOf5WeekDaysCards(filteredList, selectedUnit) // Now this is inside a Composable scope
}
