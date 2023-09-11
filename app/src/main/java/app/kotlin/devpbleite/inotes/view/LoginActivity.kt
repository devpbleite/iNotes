package app.kotlin.devpbleite.inotes.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import app.kotlin.devpbleite.inotes.databinding.ActivityLoginBinding
import app.kotlin.devpbleite.inotes.util.Util
import app.kotlin.devpbleite.inotes.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        
        binding.btnLogin.setOnClickListener { loginUser() }
        binding.btnTextViewSignUp.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    SignUpActivity::class.java
                )
            )
        }
        
        
        viewModel.loggedIn.observe(this) { loggedIn ->
            if (loggedIn) {
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
        }
    }
    
    private fun loginUser() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val isValidated = validateData(email, password)
        if (!isValidated) {
            return
        }
        viewModel.login(email, password)
    }
    
    private fun validateData(email: String?, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Invalid email."
            binding.edtEmail.requestFocus()
            return false
        }
        if (password.length < 6) {
            binding.edtPassword.error = "Password do not match."
            binding.edtPassword.requestFocus()
            return false
        }
        return true
    }
}
