package com.app.navtask.ui.composables

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.navtask.ui.viewmodel.FbViewModel
import com.app.navtask.nav.NavItem
import com.app.navtask.auth.LoginScreen
import com.app.navtask.ui.composables.tabs.MainAppScreen
import com.app.navtask.auth.RegisterScreen
import com.app.navtask.auth.SuccessScreen
import com.app.navtask.main.NotificationMessage
import com.app.navtask.ui.composables.tabs.AddTaskScreen
import com.app.navtask.ui.composables.tabs.MapScreen
import com.app.navtask.ui.viewmodel.TaskViewModel
import com.app.navtask.ui.viewmodel.UserViewModel

/**
 * Composable function that defines the navigation screens and their corresponding destinations.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NavigationScreens(
    navController: NavHostController,
    userVm: UserViewModel,
    taskVm: TaskViewModel
) {
    val vm = hiltViewModel<FbViewModel>()
    var isFromAddTask : Boolean by remember { mutableStateOf(false) }

    NotificationMessage(vm = vm)

    NavHost(navController, startDestination = NavItem.Login.path) {
        composable(NavItem.Login.path) { LoginScreen(
            onRegisterButtonClicked = {
                navController.navigate(NavItem.Register.path)
            },
            onMainAppChange = {
                navController.navigate(NavItem.MainAppScreen.path)
            },
            vm = vm,
            userVm = userVm
        ) }
        composable(NavItem.Register.path) { RegisterScreen(
            onLoginButtonClicked = {
                navController.navigate(NavItem.Login.path)
            },
            onMainAppChange = {
                isFromAddTask = false
                navController.navigate(NavItem.MainAppScreen.path)
            },
            onSuccessRegister = {
                navController.navigate(NavItem.Success.path)
            },
            vm = vm,
            userVm = userVm
        ) }
        composable(NavItem.Success.path) { SuccessScreen(
            vm = vm
        ) }
        composable(NavItem.Map.path) { MapScreen(taskVm) }
        composable(NavItem.AddTask.path) { isFromAddTask = true
            AddTaskScreen(taskVm, onAddButtonClicked = {
                navController.navigate(NavItem.MainAppScreen.path)
            }
        ) }
        composable(NavItem.MainAppScreen.path) {
            MainAppScreen(navController,vm, userVm, taskVm, isFromAddTask)
        }
    }
}