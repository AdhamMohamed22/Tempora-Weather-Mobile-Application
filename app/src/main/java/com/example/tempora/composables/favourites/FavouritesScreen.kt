package com.example.tempora.composables.favourites

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tempora.R
import com.example.tempora.data.local.WeatherDatabase
import com.example.tempora.data.local.WeatherLocalDataSource
import com.example.tempora.data.models.FavouriteLocation
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.data.response_state.FavouriteLocationsResponseState
import com.example.tempora.utils.LoadingIndicator
import com.example.tempora.utils.getAddressFromLocation


@Composable
fun FavouritesScreen(showFAB: MutableState<Boolean>, snackBarHostState: SnackbarHostState) {

    showFAB.value = true
    val context = LocalContext.current

    val favouritesScreenViewModelFactory =
        FavouritesScreenViewModel.FavouritesScreenViewModelFactory(
            Repository.getInstance(
                WeatherRemoteDataSource(RetrofitHelper.retrofit),
                WeatherLocalDataSource(WeatherDatabase.getInstance(context).getWeatherDao())
            )
        )
    val viewModel: FavouritesScreenViewModel = viewModel(factory = favouritesScreenViewModelFactory)

    val favouriteLocationState by viewModel.favouriteLocations.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.message.collect {
//            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
//        }
//    }

    LaunchedEffect(Unit) {
        viewModel.getAllFavouriteLocations()
    }

    when (favouriteLocationState) {
        is FavouriteLocationsResponseState.Loading -> LoadingIndicator()
        is FavouriteLocationsResponseState.Failed -> Text("Failed !")
        is FavouriteLocationsResponseState.Success -> DisplayFavouritesScreen(
            viewModel = viewModel,
            favouritesLocationsList = (favouriteLocationState as FavouriteLocationsResponseState.Success).favouriteLocations,
            action = { viewModel.deleteFavouriteLocation(it) },
            snackBarHostState = snackBarHostState
        )
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DisplayFavouritesScreen(viewModel: FavouritesScreenViewModel, favouritesLocationsList: List<FavouriteLocation>, action: (FavouriteLocation) -> Unit, snackBarHostState: SnackbarHostState){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.message.collect {
            val snackBarResult = snackBarHostState.showSnackbar(
                message = it,
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (snackBarResult == SnackbarResult.ActionPerformed) {
                // User clicked "Undo", restore the deleted location
                viewModel.restoreFavouriteLocation()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.white)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(favouritesLocationsList.isEmpty()){
            Image(
                painter = painterResource(id = R.drawable.nofavourites),
                contentDescription = "No Favourites Image",
                modifier = Modifier.size(200.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text(
                text = "Nothing Has Been Added To Favourites Yet!",
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.primaryColor),
                fontWeight = FontWeight.Bold
            )
        } else {
                ListOfFavouriteCards(favouritesLocationsList,action)
            }
        }
}

@Composable
fun FavouriteLocationCard(favouriteLocation: FavouriteLocation, action: (FavouriteLocation)-> Unit) {
    /*val address = getAddressFromLocation(favouriteLocation)*/
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.white))
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = favouriteLocation.country!!,
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.black),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Latitude: ${favouriteLocation.latitude}")
            Text(text = "Longitude: ${favouriteLocation.longitude}")
            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { action(favouriteLocation) },
                colors = ButtonDefaults.buttonColors(colorResource(R.color.primaryColor)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Delete",
                    style = MaterialTheme.typography.titleMedium,
                    color = colorResource(R.color.white),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun ListOfFavouriteCards(favouritesLocationsList: List<FavouriteLocation>, action: (FavouriteLocation) -> Unit){
    LazyColumn(modifier = Modifier.wrapContentSize(Alignment.Center))
    {
        items(favouritesLocationsList.size)
        {
            FavouriteLocationCard(favouritesLocationsList[it],action)
        }
    }
}

