package app.kotlin.devpbleite.inotes

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    
    private var emailEditText: EditText? = null
    private var passwordEditText: EditText? = null
    private var loginButton: Button? = null
    private var progressBar: ProgressBar? = null
    private var signUpBtnTextView: TextView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailEditText = findViewById(R.id.edt_email)
        passwordEditText = findViewById(R.id.edt_password)
        loginButton = findViewById(R.id.btn_login)
        progressBar = findViewById(R.id.progress_bar)
        signUpBtnTextView = findViewById(R.id.btn_text_view_sign_up)
        
        loginButton!!.setOnClickListener { loginUser() }
        signUpBtnTextView!!.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignUpActivity::class.java
                )
            )
        }
    }
    
    private fun loginUser() {
        val email = emailEditText!!.text.toString().trim()
        val password = passwordEditText!!.text.toString().trim()
        val isValidated = validateData(email, password)
        if (!isValidated) {
            return
        }
        loginAccountInFirebase(email, password)
    }
    
    private fun loginAccountInFirebase(email: String, password: String) {
        changeInProgress(true)
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword((email), (password))
            .addOnCompleteListener { task ->
                changeInProgress(false)
                if (task.isSuccessful) {
                    if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        )
                        finish()
                    } else {
                        Util.showToast(
                            this@LoginActivity,
                            "E-mail is not verified. Please verify your e-mail."
                        )
                    }
                } else {
                    Util.showToast(
                        this@LoginActivity, "Error: " + task.exception!!
                            .localizedMessage
                    )
                }
            }
    }
    
    private fun changeInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar!!.visibility = View.VISIBLE
            loginButton!!.visibility = View.GONE
        } else {
            progressBar!!.visibility = View.GONE
            loginButton!!.visibility = View.VISIBLE
        }
    }
    
    private fun validateData(email: String?, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText!!.error = "Invalid email."
            emailEditText!!.requestFocus()
            return false
        }
        if (password.length < 6) {
            passwordEditText!!.error = "Password do not match."
            passwordEditText!!.requestFocus()
            return false
        }
        return true
    }
}