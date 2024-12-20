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

    fun updateName(email: String, name: String) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            dao.updateName(email, name)
        }
    }

    fun incrementTasksInProgress(email: String) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            dao.incrementTasksInProgress(email)
        }
    }

    fun incrementTasksCompleted(email: String) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            dao.incrementTasksCompleted(email)
        }
    }

    fun decrementTasksInProgress(email: String) {
        viewModelScope.launch(Dispatchers.Unconfined) {
            dao.decrementTasksInProgress(email)
        }
    }
}