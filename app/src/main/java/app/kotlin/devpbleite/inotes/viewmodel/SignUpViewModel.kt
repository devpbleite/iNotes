package app.kotlin.devpbleite.inotes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class SignUpViewModel : ViewModel() {
    
    fun createAccount(email: String, password: String) = liveData(Dispatchers.IO) {
        val firebaseAuth = FirebaseAuth.getInstance()
        
        try {
            val userCredential = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = userCredential.user
            
            if (user != null) {
                user.sendEmailVerification().await()
                emit(SignUpResult.Success)
            } else {
                emit(SignUpResult.Error("Failed to create user."))
            }
        } catch (e: Exception) {
            emit(SignUpResult.Error(e.message ?: "An error occurred."))
        }
    }
    
    sealed class SignUpResult {
        object Success : SignUpResult()
        data class Error(val message: String) : SignUpResult()
    }
}
