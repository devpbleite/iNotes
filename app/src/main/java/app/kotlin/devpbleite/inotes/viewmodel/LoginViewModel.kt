package app.kotlin.devpbleite.inotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {
    
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn
    
    fun login(email: String, password: String) {
        
        val auth = FirebaseAuth.getInstance()
        
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    _loggedIn.value = (user != null) && user.isEmailVerified
                } else {
                    _loggedIn.value = false
                }
            }
    }
}