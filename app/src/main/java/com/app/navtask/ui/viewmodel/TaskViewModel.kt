package com.app.navtask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.navtask.ui.dao.TaskDao
import com.app.navtask.ui.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskDao : TaskDao
) : ViewModel() {

        fun addTask(task: Task) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.upsert(task)
            }
        }

        suspend fun getAllTasks(): List<Task> {
            return taskDao.getAllTasks()
        }

        suspend fun getTaskById(id: Int): Task? {
            return taskDao.getTaskById(id)
        }

        fun deleteTaskById(id: Int) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.deleteTaskById(id)
            }
        }

        suspend fun searchTasks(query: String): List<Task> {
            return taskDao.searchTasks(query)
        }

        suspend fun getFirstTask(): Task? {
            return taskDao.getFirstTask()
        }

        fun updateTask(id: Int, title: String, description: String, location: String, priority: Int, date: String) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.updateTask(id, title, description, location, priority, date)
            }
        }
}