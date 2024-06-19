package com.app.navtask.ui.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.navtask.ui.model.Task

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsert(task: Task)

    @Query("SELECT * FROM tasks ORDER BY date ASC, priority DESC")
    suspend fun getAllTasks(): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): Task?

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

}