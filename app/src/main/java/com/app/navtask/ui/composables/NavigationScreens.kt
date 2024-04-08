package com.app.navtask.ui.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.navtask.FbViewModel
import com.app.navtask.FsViewModel
import com.app.navtask.nav.NavItem
import com.app.navtask.auth.LoginScreen
import com.app.navtask.ui.composables.tabs.MainAppScreen
import com.app.navtask.auth.RegisterScreen
import com.app.navtask.auth.SuccessScreen
import com.app.navtask.main.NotificationMessage
import com.app.navtask.ui.composables.tabs.MapScreen
import com.app.navtask.ui.model.Db
import com.app.navtask.ui.model.UserViewModel
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

/**
 * Composable function that defines the navigation screens and their corresponding destinations.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@Composable
fun NavigationScreens(navController: NavHostController, userVm: UserViewModel) {
    val vm = hiltViewModel<FbViewModel>()

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
        composable(NavItem.Map.path) { MapScreen() }
        composable(NavItem.MainAppScreen.path) {
            MainAppScreen(navController, vm, userVm)
        }
    }
}