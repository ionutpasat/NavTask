package com.app.navtask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.app.navtask.ui.dao.UserDao
import com.app.navtask.ui.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserViewModel(
    private val dao: UserDao
) : ViewModel() {

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            dao.upsert(user)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }

    suspend fun getAllUsers(): List<User> {
        return dao.getAllUsers()
    }

    suspend fun updateThemePreference(email: String, themePreference: Boolean) {
        dao.updateThemePreference(email, themePreference)
    }
}