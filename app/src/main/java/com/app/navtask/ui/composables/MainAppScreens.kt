package com.app.navtask.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.navtask.nav.NavItem
import com.app.navtask.ui.composables.tabs.HomeScreen
import com.app.navtask.ui.composables.tabs.ListScreen
import com.app.navtask.ui.composables.tabs.ProfileScreen
import com.app.navtask.ui.composables.tabs.SearchScreen
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.app.navtask.ui.viewmodel.UserViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun MainAppScreens(bottomNavController : NavHostController,
                   navController : NavHostController,
                   vm : FbViewModel,
                   userVm: UserViewModel,
                   taskVm : TaskViewModel,
                   onThemeButtonClicked: () -> Unit = {}
) {
    NavHost(bottomNavController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen(
            taskVm,
            vm,
            onAddTaskButtonClicked = {
                navController.navigate(NavItem.AddTask.path)
            }
        ) }
        composable(NavItem.Search.path) { SearchScreen(
            taskVm,
            vm,
            onTaskButtonClicked = { taskId, temp, precip ->
                navController.navigate(NavItem.TaskDetails.path + "/$taskId" + "/$temp" + "/$precip")
            }
        ) }
        composable(NavItem.List.path) { ListScreen(
            taskVm,
            userVm,
            vm,
            onTaskButtonClicked = { taskId, temp, precip ->
                navController.navigate(NavItem.TaskDetails.path + "/$taskId" + "/$temp" + "/$precip")
            }
        ) }
        composable(NavItem.Profile.path) { ProfileScreen(
            vm,
            userVm,
            onLogoutButtonClicked = {
                vm.auth.signOut()
                navController.navigate(NavItem.Login.path)
            },
            onThemeButtonClicked
        ) }
    }
}