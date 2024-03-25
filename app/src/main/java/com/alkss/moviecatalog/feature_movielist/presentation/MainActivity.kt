package com.alkss.moviecatalog.feature_movielist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.alkss.moviecatalog.feature_movielist.presentation.home.HomeScreen
import com.alkss.moviecatalog.feature_movielist.presentation.util.Screen
import com.alkss.moviecatalog.ui.theme.BaseAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BaseAppTheme {
                Surface {
                    val navController = rememberNavController()
                    NavHost(modifier = Modifier.padding(vertical = 20.dp), navController = navController, startDestination = Screen.HomeScreen.route){
                        composable(
                            route = Screen.HomeScreen.route
                        ){
                            HomeScreen()
                        }
                    }
                }
            }
        }
    }
}
