package app.kotlin.devpbleite.inotes

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignUpActivity : AppCompatActivity() {
    
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var confirmPasswordEditText: EditText? = null
    private var createAccountButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var loginBtnTextView: TextView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        
        emailEditText = findViewById(R.id.edt_email)
        passwordEditText = findViewById(R.id.edt_password)
        confirmPasswordEditText = findViewById(R.id.edt_confirm_password)
        createAccountButton = findViewById(R.id.btn_create_account)
        progressBar = findViewById(R.id.progress_bar)
        loginBtnTextView = findViewById(R.id.btn_text_view_login)
        
        createAccountButton!!.setOnClickListener { createAccount() }
        loginBtnTextView!!.setOnClickListener { finish() }
    }
    
    private fun createAccount() {
        val email = emailEditText!!.text.toString().trim()
        val password = passwordEditText!!.text.toString().trim()
        val confirmPassword = confirmPasswordEditText!!.text.toString().trim()
        
        val isValidated = validateData(email, password, confirmPassword)
        if (!isValidated) {
            return
        }
        createAccountInFirebase(email, password)
    }
    
    private fun createAccountInFirebase(email: String?, password: String?) {
        changeInProgress(true)
        
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(
                this@SignUpActivity
            ) { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    Util.showToast(this@SignUpActivity,
                        "Account created successfully! Check your e-mail to verify.")
                    firebaseAuth.currentUser!!.sendEmailVerification()
                    firebaseAuth.signOut()
                    finish()
                } else {
                    Util.showToast(this@SignUpActivity,"Error: " + task.exception!!.message)
                }
            }
    }
    
    private fun changeInProgress(isInProgress: Boolean) {
        if (isInProgress) {
            progressBar!!.visibility = View.VISIBLE
            createAccountButton!!.visibility = View.GONE
        } else {
            progressBar!!.visibility = View.GONE
            createAccountButton!!.visibility = View.VISIBLE
        }
    }
    
    private fun validateData(
        email: String?,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText!!.error = "Invalid e-mail."
            return false
        }
        if (password.length < 6) {
            passwordEditText!!.error = "Password must be at least 6 characters."
            return false
        }
        if (password != confirmPassword) {
            confirmPasswordEditText!!.error = "Passwords do not match."
            return false
        }
        return true
    }
}