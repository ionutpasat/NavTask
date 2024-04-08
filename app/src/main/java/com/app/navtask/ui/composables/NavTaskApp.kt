package com.app.navtask.ui.composables

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.room.RoomDatabase
import com.app.navtask.ui.model.UserViewModel

/**
 * Composable function that represents the main screen of the application.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavTaskApp(navController: NavHostController, userVm: UserViewModel) {
//    Scaffold(bottomBar = {
//        BottomAppBar { BottomNavigationBar(navController = navController) }
//    }) { NavigationScreens(navController = navController) }
    NavigationScreens(navController, userVm)
}