package com.app.navtask.ui.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.navtask.ui.model.Task

@Dao
interface TaskDao {

    @Upsert
    suspend fun upsert(task: Task)

    @Query("SELECT * FROM tasks WHERE user = :user ORDER BY date ASC, priority DESC")
    suspend fun getAllTasks(user: String): List<Task>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    suspend fun getTaskById(id: Int): Task?

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTaskById(id: Int)

    @Query("SELECT * FROM tasks WHERE (title LIKE :query OR description LIKE :query) AND user = :user")
    suspend fun searchTasks(query: String, user: String): List<Task>

    @Query("SELECT * FROM tasks WHERE user = :user ORDER BY date ASC, priority DESC LIMIT 1")
    suspend fun getFirstTask(user: String): Task?

    @Query("UPDATE tasks SET title = :title, description = :description, location = :location, priority = :priority, date = :date WHERE id = :id")
    suspend fun updateTask(id: Int, title: String, description: String, location: String, priority: Int, date: String)

    @Query("UPDATE tasks SET type = :type WHERE id = :id")
    suspend fun updateTaskType(id: Int, type: String)

    @Query("UPDATE tasks SET latitude = :latitude, longitude = :longitude WHERE id = :id")
    suspend fun updateTaskLocation(id: Int, latitude: Double, longitude: Double)

}