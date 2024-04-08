package com.app.navtask.ui.model

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 1)
abstract class Db : RoomDatabase(){
    abstract val userDao: UserDao
}