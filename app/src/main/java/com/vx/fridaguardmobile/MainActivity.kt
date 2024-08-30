package com.vx.fridaguardmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.vx.fridaguardmobile.ui.screens.PageSplash
import com.vx.fridaguardmobile.ui.theme.FridaGuardMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FridaGuardMobileTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val systemUiController = rememberSystemUiController()
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            PageSplash(navController = navController)
        }
    }

    systemUiController.statusBarDarkContentEnabled = false
    systemUiController.navigationBarDarkContentEnabled = false
}