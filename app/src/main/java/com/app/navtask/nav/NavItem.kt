package com.app.navtask.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Task

sealed class NavItem {
    object Home :
        Item(path = NavPath.HOME.toString(), title = NavTitle.HOME, icon = Icons.Default.Home)

    object Search :
        Item(path = NavPath.SEARCH.toString(), title = NavTitle.SEARCH, icon = Icons.Default.Search)

    object List :
        Item(path = NavPath.LIST.toString(), title = NavTitle.LIST, icon = Icons.Default.List)

    object Profile :
        Item(
            path = NavPath.PROFILE.toString(), title = NavTitle.PROFILE, icon = Icons.Default.Person)

    object Login :
        Item(
            path = NavPath.LOGIN.toString(), title = NavTitle.LOGIN, icon = Icons.Default.Login)

    object Register :
        Item(
            path = NavPath.REGISTER.toString(), title = NavTitle.REGISTER, icon = Icons.Default.HowToReg)

    object Success :
        Item(
            path = NavPath.SUCCESS.toString(), title = NavTitle.REGISTER, icon = Icons.Default.Check)

    object Map:
        Item(
            path = NavPath.MAP.toString(), title = NavTitle.MAP, icon = Icons.Default.Map)

    object AddTask :
        Item(
            path = NavPath.ADDTASK.toString(), title = NavTitle.ADDTASK, icon = Icons.Default.Task)

    object MainAppScreen :
        Item(
            path = NavPath.MAINAPP.toString(), title = NavTitle.MAINAPP, icon = Icons.Default.Domain)
}