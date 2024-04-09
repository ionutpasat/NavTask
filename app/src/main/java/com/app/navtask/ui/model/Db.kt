package com.app.navtask.ui.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.navtask.ui.dao.TaskDao
import com.app.navtask.ui.dao.UserDao

@Database(entities = [User::class, Task::class], version = 2)
abstract class Db : RoomDatabase(){
    abstract val userDao: UserDao
    abstract val taskDao: TaskDao
}