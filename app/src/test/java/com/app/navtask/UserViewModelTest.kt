package com.app.navtask

import com.app.navtask.ui.dao.UserDao
import com.app.navtask.ui.model.User
import com.app.navtask.ui.viewmodel.UserViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class UserViewModelTest {
    private val userDao: UserDao = mock(UserDao::class.java)
    private fun createUserViewModel() = UserViewModel(userDao)

    @Test
    fun testGetAllUsers() = runBlocking {
        val mockUsers = listOf(User(1, "john@test.com", "John Doe"),
            User(2, "jane@test.com", "Jane Doe"))
        Mockito.`when`(userDao.getAllUsers()).thenReturn(mockUsers)
        val viewModel = createUserViewModel()
        val result = viewModel.getAllUsers()
        assert(result == mockUsers)
    }

    @Test
    fun testGetUserByEmail() = runBlocking {
        val mockUser = User(1, "john@test.com", "John Doe")
        Mockito.`when`(userDao.getUserByEmail("john@test.com")).thenReturn(mockUser)
        val viewModel = createUserViewModel()
        val result = viewModel.getUserByEmail("john@test.com")
        assert(result == mockUser)
    }


    @Test
    fun testAddUser() = runBlocking {

        val viewModel = createUserViewModel()
        val user = User(1, "john@test.com", "John Doe")

        viewModel.addUser(user)
        verify(userDao).upsert(user)
    }

}