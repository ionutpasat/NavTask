package com.app.navtask.ui.composables

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.navtask.FbViewModel
import com.app.navtask.nav.NavItem
import com.app.navtask.ui.composables.tabs.HomeScreen
import com.app.navtask.ui.composables.tabs.ListScreen
import com.app.navtask.ui.composables.tabs.MapScreen
import com.app.navtask.ui.composables.tabs.ProfileScreen
import com.app.navtask.ui.composables.tabs.SearchScreen

@Composable
fun MainAppScreens(bottomNavController : NavHostController, navController : NavHostController, vm : FbViewModel) {
    NavHost(bottomNavController, startDestination = NavItem.Home.path) {
        composable(NavItem.Home.path) { HomeScreen() }
        composable(NavItem.Search.path) { SearchScreen() }
        composable(NavItem.List.path) { ListScreen(
            onButtonClicked = {
                navController.navigate(NavItem.Map.path)
            }
        ) }
        composable(NavItem.Profile.path) { ProfileScreen(
            vm,
            onLogoutButtonClicked = {
                navController.navigate(NavItem.Login.path)
            }
        ) }
    }
}