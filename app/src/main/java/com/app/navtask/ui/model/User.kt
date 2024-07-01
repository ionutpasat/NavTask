package com.app.navtask.ui.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "users", [Index(value = ["email"], unique = true)])
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val email: String = "",
    var name: String = "",
    var profileImageUri: String = "",
    var tasksCompleted: Int = 0,
    var tasksInProgress: Int = 0,
)
