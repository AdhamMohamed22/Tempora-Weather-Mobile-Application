package com.example.tempora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.tempora.composables.splashscreen.SplashScreen
import com.example.tempora.composables.home.HomeScreenViewModel
import com.example.tempora.data.remote.RetrofitHelper
import com.example.tempora.data.remote.WeatherRemoteDataSource
import com.example.tempora.data.repository.Repository
import com.example.tempora.utils.ScreenRoutes
import com.example.tempora.utils.SetupAppNavigation
import com.exyte.animatednavbar.AnimatedNavigationBar
import com.exyte.animatednavbar.animation.balltrajectory.Parabolic
import com.exyte.animatednavbar.animation.indendshape.Height
import com.exyte.animatednavbar.animation.indendshape.shapeCornerRadius
import com.exyte.animatednavbar.utils.noRippleClickable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            var displaySplashScreen by remember { mutableStateOf(true) }
            if (displaySplashScreen){
                SplashScreen(onComplete = {displaySplashScreen = false})
            } else {
                MainScreen()
            }
        }
    }
}


@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navigationBarItems = remember { NavigationBarItems.values() }
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        modifier = Modifier.padding(all = 0.dp),
        bottomBar = {
            AnimatedNavigationBar(
                modifier = Modifier.height(64.dp),
                selectedIndex = selectedIndex,
                cornerRadius = shapeCornerRadius(36.dp),
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
                                Color.LightGray
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            SetupAppNavigation(navController)
        }
    }
}

enum class NavigationBarItems(val icon: ImageVector,val screen: ScreenRoutes){
    Home(icon = Icons.Default.Home,ScreenRoutes.Home),
    Favourites(icon = Icons.Default.Favorite,ScreenRoutes.Favourites),
    Alerts(icon = Icons.Default.Notifications,ScreenRoutes.Alarms),
    Settings(icon = Icons.Default.Settings,ScreenRoutes.Settings),
}
