package com.app.navtask

import androidx.lifecycle.ViewModel
import com.app.navtask.ui.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FsViewModel @Inject constructor(
    val db: FirebaseFirestore
) : ViewModel(){
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
}