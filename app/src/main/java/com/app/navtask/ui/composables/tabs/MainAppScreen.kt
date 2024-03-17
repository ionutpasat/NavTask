package com.app.navtask.ui.composables.tabs

import android.annotation.SuppressLint
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.navtask.ui.composables.BottomNavigationBar
import com.app.navtask.ui.composables.MainAppScreens
import com.app.navtask.ui.composables.NavigationScreens

/**
 * Composable function that represents the home screen of the application.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAppScreen(navController : NavHostController) {
    val bottomBarController = rememberNavController()
    Scaffold(bottomBar = {
        BottomAppBar { BottomNavigationBar(bottomBarController) }
    }) { MainAppScreens(bottomBarController, navController) }
}