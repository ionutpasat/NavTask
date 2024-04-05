package com.app.navtask

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.app.navtask.ui.components.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FbViewModel @Inject constructor(
    val auth : FirebaseAuth
) : ViewModel(){
    val signedIn = mutableStateOf(false)
    val inProgress = mutableStateOf(false)
    val popupNotification = mutableStateOf<Event<String>?>(null)

    fun onSignup(email: String, password: String){
        inProgress.value = true
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                inProgress.value = false
                if (task.isSuccessful) {
                    signedIn.value = true
                    handleException(task.exception, "Signup successful")
                } else {
                    handleException(task.exception, "Signup failed")
                }
                inProgress.value = false
            }
    }

    fun login(email: String, password: String){
        inProgress.value = true
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    signedIn.value = true
                    handleException(task.exception, "Login successful")
                } else {
                    handleException(task.exception, "Login failed")
                }
                inProgress.value = false
            }
    }

    fun getSignedInUser() : FirebaseUser? {
        return auth.currentUser
    }

    fun handleException(exception: Exception? = null, customMessage: String? = ""){
        exception?.printStackTrace()
        val errorMsg = exception?.localizedMessage ?: ""
        val message = if (customMessage.isNullOrEmpty()) errorMsg else "$customMessage: $errorMsg"
        popupNotification.value = Event(message)
    }

}