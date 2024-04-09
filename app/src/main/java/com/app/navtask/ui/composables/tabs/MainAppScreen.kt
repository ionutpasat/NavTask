package com.app.navtask.ui.composables.tabs

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.ui.composables.BottomNavigationBar
import com.app.navtask.ui.composables.MainAppScreens
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.app.navtask.ui.viewmodel.UserViewModel

/**
 * Composable function that represents the home screen of the application.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAppScreen(navController : NavHostController,
                  vm : FbViewModel, userVm: UserViewModel,
                  taskVm : TaskViewModel,
                  isFromAddTask : Boolean = false
) {

    val bottomBarController = rememberNavController()
    Scaffold(bottomBar = {
        BottomAppBar { BottomNavigationBar(bottomBarController, isFromAddTask) }
    }) { MainAppScreens(bottomBarController, navController, vm, userVm, taskVm) }
}