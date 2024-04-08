package com.app.navtask.ui.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class UserViewModel(
    private val dao: UserDao
) : ViewModel() {

    fun addUser(user: User) {
        viewModelScope.launch {
            dao.upsert(user)
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return dao.getUserByEmail(email)
    }

    fun getAllUsers(): List<User>? {
        return liveData<List<User>> {
            emit(dao.getAllUsers())
        }.value
    }
}