package com.app.navtask

import com.app.navtask.ui.dao.TaskDao
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.model.User
import com.app.navtask.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class TaskViewModelTest {
    private val taskDao: TaskDao = Mockito.mock(TaskDao::class.java)
    private fun createTaskViewModel() = TaskViewModel(taskDao)
    private val user: User = User(1, "email@gmail.com", "John Doe")

    @Test
    fun testGetAllTasks() = runBlocking {

        val mockTasks = listOf(
            Task(1, "Task 1", "Description 1", 1, "Location 1",1.0, 2.0, "2021-01-01", "Work", user),
            Task(2, "Task 2", "Description 2", 2, "Location 2",1.0, 2.0, "2021-01-02", "Personal", user)
        )
        Mockito.`when`(taskDao.getAllTasks()).thenReturn(mockTasks)
        val viewModel = createTaskViewModel()
        val result = viewModel.getAllTasks()
        assert(result == mockTasks)
    }

    @Test
    fun testGetTaskById() = runBlocking {

        val mockTask = Task(1, "Task 1", "Description 1", 1, "Location 1",1.0, 2.0, "2021-01-01", "Work", user)
        Mockito.`when`(taskDao.getTaskById(1)).thenReturn(mockTask)
        val viewModel = createTaskViewModel()
        val result = viewModel.getTaskById(1)
        assert(result == mockTask)
    }


    @Test
    fun testAddTask() = runBlocking {

        val viewModel = createTaskViewModel()
        val task = Task(1, "Task 1", "Description 1", 1, "Location 1",1.0, 2.0, "2021-01-01", "Work", user)

        viewModel.addTask(task)
        Mockito.verify(taskDao).upsert(task)
    }
}