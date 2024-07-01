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

        suspend fun getAllTasks(user: String): List<Task> {
            return taskDao.getAllTasks(user)
        }

        suspend fun getTaskById(id: Int): Task? {
            return taskDao.getTaskById(id)
        }

        fun deleteTaskById(id: Int) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.deleteTaskById(id)
            }
        }

        suspend fun searchTasks(query: String, user: String): List<Task> {
            return taskDao.searchTasks(query, user)
        }

        suspend fun getFirstTask(user: String): Task? {
            return taskDao.getFirstTask(user)
        }

        fun updateTask(id: Int, title: String, description: String, location: String, priority: Int, date: String) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.updateTask(id, title, description, location, priority, date)
            }
        }

        fun updateTaskType(id: Int, type: String) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.updateTaskType(id, type)
            }
        }

        fun updateTaskLocation(id: Int, latitude: Double, longitude: Double) {
            viewModelScope.launch(Dispatchers.Unconfined) {
                taskDao.updateTaskLocation(id, latitude, longitude)
            }
        }
}