package com.app.navtask

import com.app.navtask.ui.dao.TaskDao
import com.app.navtask.ui.model.Task
import com.app.navtask.ui.viewmodel.TaskViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito

class TaskViewModelTest {
    private val taskDao: TaskDao = Mockito.mock(TaskDao::class.java)
    private fun createTaskViewModel() = TaskViewModel(taskDao)

    @Test
    fun testGetAllTasks() = runBlocking {

        val mockTasks = listOf(
            Task(1, "Task 1", "Description 1", 1, "Location 1", "2021-01-01"),
            Task(2, "Task 2", "Description 2", 2, "Location 2", "2021-01-02")
        )
        Mockito.`when`(taskDao.getAllTasks()).thenReturn(mockTasks)
        val viewModel = createTaskViewModel()
        val result = viewModel.getAllTasks()
        assert(result == mockTasks)
    }

    @Test
    fun testGetTaskById() = runBlocking {

        val mockTask = Task(1, "Task 1", "Description 1", 1, "Location 1", "2021-01-01")
        Mockito.`when`(taskDao.getTaskById(1)).thenReturn(mockTask)
        val viewModel = createTaskViewModel()
        val result = viewModel.getTaskById(1)
        assert(result == mockTask)
    }


    @Test
    fun testAddTask() = runBlocking {

        val viewModel = createTaskViewModel()
        val task = Task(1, "Task 1", "Description 1", 1, "Location 1", "2021-01-01")

        viewModel.addTask(task)
        Mockito.verify(taskDao).upsert(task)
    }
}