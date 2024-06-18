package com.app.navtask.ui.composables

import android.annotation.SuppressLint
import android.location.Location
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.app.navtask.ui.viewmodel.UserViewModel

/**
 * Composable function that represents the main screen of the application.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NavTaskApp(
    navController: NavHostController,
    userVm: UserViewModel, taskVm:
               TaskViewModel,
    location: Location?,
    onThemeButtonClicked: () -> Unit = {}
) {
//    Scaffold(bottomBar = {
//        BottomAppBar { BottomNavigationBar(navController = navController) }
//    }) { NavigationScreens(navController = navController) }
    NavigationScreens(navController, userVm, taskVm, location, onThemeButtonClicked)
}