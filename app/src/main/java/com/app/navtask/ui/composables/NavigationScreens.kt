package com.app.navtask.ui.composables

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.navtask.FbViewModel
import com.app.navtask.nav.NavItem
import com.app.navtask.auth.LoginScreen
import com.app.navtask.ui.composables.tabs.MainAppScreen
import com.app.navtask.auth.RegisterScreen
import com.app.navtask.auth.SuccessScreen

/**
 * Composable function that defines the navigation screens and their corresponding destinations.
 *
 * @param navController The navigation controller used for handling navigation between screens.
 */
@Composable
fun NavigationScreens(navController: NavHostController) {
    val vm = hiltViewModel<FbViewModel>()
    NavHost(navController, startDestination = NavItem.Login.path) {
        composable(NavItem.Login.path) { LoginScreen(
            onRegisterButtonClicked = {
                navController.navigate(NavItem.Register.path)
            },
            onMainAppChange = {
                navController.navigate(NavItem.MainAppScreen.path)
            },
            vm = vm
        ) }
        composable(NavItem.Register.path) { RegisterScreen(
            onLoginButtonClicked = {
                navController.navigate(NavItem.Login.path)
            },
            onMainAppChange = {
                navController.navigate(NavItem.MainAppScreen.path)
            },
            vm = vm
        ) }
        composable(NavItem.Success.path) { SuccessScreen(
            vm = vm
        ) }
        composable(NavItem.MainAppScreen.path) {
            MainAppScreen(navController)
        }
    }
}