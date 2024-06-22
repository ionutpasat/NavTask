package com.app.navtask.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val description : String,
    val priority : Int,
    val location : String,
    val latitude : Double,
    val longitude : Double,
    val date : String,
    val type : String
)