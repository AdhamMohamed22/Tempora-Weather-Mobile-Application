package com.example.tempora

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.example.tempora.composables.alarms.notification.createNotificationChannel
import com.example.tempora.composables.alarms.notification.stopNotificationSound
import com.example.tempora.composables.settings.utils.LocalizationHelper
import com.example.tempora.composables.settings.PreferencesManager
import com.example.tempora.composables.splashscreen.SplashScreen
import com.example.tempora.utils.navigation.ScreenRoutes
import com.example.tempora.utils.navigation.SetupAppNavigation
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

const val REQUEST_LOCATION_CODE = 2005

class MainActivity : ComponentActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationState: MutableState<Location>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Notification
        createNotificationChannel(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
        }
        // Check if the activity was opened from a notification
        val notificationId = intent.getIntExtra("notification_id", -1)
        if (notificationId != -1) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId) // Cancel the notification

            // Stop the alarm sound
            stopNotificationSound()
        }

        val preferencesManager = PreferencesManager.getInstance(this)
        // Use runBlocking to get the saved language synchronously
        val savedLanguage = runBlocking {
            preferencesManager.getPreference(PreferencesManager.LANGUAGE_KEY, "English").first()
        }
        val languageCode = if (savedLanguage == "Arabic") "ar" else "en"
        LocalizationHelper.setLocale(this, languageCode)

        setContent {
            locationState = remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER)) }

            var displaySplashScreen by remember { mutableStateOf(true) }
            if (displaySplashScreen){
                SplashScreen(onComplete = {displaySplashScreen = false})
            } else {
                MainScreen(locationState.value)
            }


            // Notification
            val navController = rememberNavController()
            val deepLinkUri = intent?.data?.toString()
            LaunchedEffect(deepLinkUri) {
                deepLinkUri?.let { uri ->
                    if (uri.startsWith("D:\\TEMPORA Project\\TEMPORA\\app\\src\\main\\java\\com\\example\\tempora\\composables\\home")) {
                        navController.navigate("Home Screen")
                    }
                }
            }

//            val serviceIntent = Intent(this, NotificationService::class.java)
//            startService(serviceIntent)
        }
    }


    override fun onStart() {
        super.onStart()
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                getFreshLocation()
            } else {
                enableLocationServices()
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getFreshLocation()
            }
        }
    }

    private fun checkPermissions(): Boolean {
        return checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getFreshLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(0).apply {
                setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            }.build(),
            object : LocationCallback() {
                override fun onLocationResult(location: LocationResult) {
                    super.onLocationResult(location)
                    locationState.value = location.lastLocation!!
                }
            }, Looper.myLooper()
        )
    }


    private fun enableLocationServices() {
        Toast.makeText(this, "Turn on Location", Toast.LENGTH_LONG).show()
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(location: Location) {

    val navController = rememberNavController()
    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableStateOf(0) }

    val snackBarHostState = remember { SnackbarHostState() }

    val showFAB = remember { mutableStateOf(false) }
    val favouritesFAB = remember { mutableStateOf(true) }
    val showAlarmsBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.padding(all = 0.dp),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        floatingActionButton = {
            if(showFAB.value){
                FloatingActionButton(
                    onClick = {
                        /* Handle FAB click */
                        if(favouritesFAB.value){
                            navController.navigate("MapScreen/${true}")
                        } else {
                            showAlarmsBottomSheet.value = true
                        }
                    },
                    containerColor = colorResource(R.color.primaryColor),
                    modifier = Modifier.offset(x = 5.dp, y = 10.dp)
                ) {
                    if(favouritesFAB.value) { Icon(Icons.Default.Favorite, contentDescription = "Add To Favourites", tint = colorResource(R.color.white)) }
                    else { Icon(Icons.Default.Notifications, contentDescription = "Add To Alarms", tint = colorResource(R.color.white)) }
                }
            }
        },
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(64.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(20.dp),
                ballAnimation = Parabolic(tween(300)),
                indentAnimation = Height(tween(300)),
                barColor =  colorResource(id = R.color.primaryColor),
                ballColor = colorResource(id = R.color.primaryColor),
            ) {
                navigationBarItems.forEachIndexed{ index , item ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .noRippleClickable { selectedIndex = item.ordinal }
                            .clickable {
                                selectedIndex = index
                                navController.navigate(item.screen.route)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            modifier = Modifier.size(26.dp),
                            imageVector = item.icon,
                            contentDescription = "Bottom Bar Icon",
                            tint = if (selectedIndex == item.ordinal)
                                colorResource(R.color.white)
                            else
                                colorResource(R.color.secondaryColor)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            SetupAppNavigation(navController,location,showFAB,snackBarHostState,favouritesFAB,showAlarmsBottomSheet)
        }
    }
}

enum class NavigationBarItems(val icon: ImageVector,val screen: ScreenRoutes){
    Home(icon = Icons.Default.Home, ScreenRoutes.Home),
    Favourites(icon = Icons.Default.Favorite, ScreenRoutes.Favourites),
    Alerts(icon = Icons.Default.Notifications, ScreenRoutes.Alarms),
    Settings(icon = Icons.Default.Settings, ScreenRoutes.Settings),
}
