package com.app.navtask.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.navtask.ui.dao.TaskDao
import com.app.navtask.ui.model.Task
import kotlinx.coroutines.launch

class TaskViewModel(
    private val taskDao : TaskDao
) : ViewModel() {

        fun addTask(task: Task) {
            viewModelScope.launch {
                taskDao.upsert(task)
            }
        }

        suspend fun getAllTasks(): List<Task> {
            return taskDao.getAllTasks()
        }

        suspend fun getTaskById(id: Int): Task? {
            return taskDao.getTaskById(id)
        }
}