package com.app.navtask.ui.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.app.navtask.ui.model.User

@Dao
interface UserDao {
    @Upsert
    suspend fun upsert(user: User)

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<User>

    @Query("UPDATE users SET themePreference = :themePreference WHERE email = :email")
    suspend fun updateThemePreference(email: String, themePreference: Boolean)


}