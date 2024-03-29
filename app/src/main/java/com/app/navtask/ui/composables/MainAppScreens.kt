package com.app.navtask.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.navtask.nav.NavItem
import com.app.navtask.ui.composables.tabs.HomeScreen
import com.app.navtask.ui.composables.tabs.ListScreen
import com.app.navtask.ui.composables.tabs.ProfileScreen
import com.app.navtask.ui.composables.tabs.SearchScreen

@Composable
fun MainAppScreens(bottomNavController : NavHostController, navController : NavHostController) {
    NavHost(bottomNavController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.Search.path) { SearchScreen() }
        composable(NavItem.List.path) { ListScreen() }
        composable(NavItem.Profile.path) { ProfileScreen(
            "User Name",
            "user@email.com",
            onLogoutButtonClicked = {
                navController.navigate(NavItem.Login.path)
            }
        ) }
    }
}