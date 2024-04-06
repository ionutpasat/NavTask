package com.app.navtask

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.navtask.ui.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class FsViewModel @Inject constructor(
    val db: FirebaseFirestore
) : ViewModel(){
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun addUser(user: User){
        db.collection("emails")
            .document(user.email)
            .get()
            .addOnSuccessListener { document ->
                if (!document.exists()) {
                    // No user with the same email exists, add the user
                    db.collection("users")
                        .add(user)
                        .addOnSuccessListener { documentReference ->
                            println("DocumentSnapshot added with ID: ${documentReference.id}")
                            // Also add the email to the "emails" collection
                            db.collection("emails")
                                .document(user.email)
                                .set(user)
                                .addOnSuccessListener {
                                    println("Email added successfully")
                                }
                                .addOnFailureListener { e ->
                                    println("Error adding email: $e")
                                }
                        }
                        .addOnFailureListener { e ->
                            println("Error adding document: $e")
                        }
                } else {
                    println("User with the same email already exists")
                }
            }
            .addOnFailureListener { e ->
                println("Error checking document: $e")
            }
    }

    fun fetchUserByEmail(email: String) {
        viewModelScope.launch {
            val user = getUserByEmail(email)
            _user.value = user
        }
    }

    private suspend fun getUserByEmail(email: String): User? {
        try {
            val document = db.collection("emails")
                .document(email)
                .get()
                .await()

            return if (document.exists()) {
                // User with the given email exists
                document.toObject(User::class.java)
            } else {
                // No user with the given email exists
                User("", "")
            }
        } catch (e: Exception) {
            return null
        }
    }
}