package app.kotlin.devpbleite.inotes.view

import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import app.kotlin.devpbleite.inotes.databinding.ActivitySignUpBinding
import app.kotlin.devpbleite.inotes.util.Util
import app.kotlin.devpbleite.inotes.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        binding.btnCreateAccount.setOnClickListener { createAccount() }
        binding.btnTextViewLogin.setOnClickListener { finish() }
    }
    
    private fun createAccount() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
        
        val isValidated = validateData(email, password, confirmPassword)
        if (!isValidated) {
            return
        }
        
        viewModel.createAccount(email, password).observe(this, Observer { result ->
            when (result) {
                is SignUpViewModel.SignUpResult.Success -> {
                    Util.showToast(
                        this@SignUpActivity,
                        "Account created successfully! Check your e-mail to verify."
                    )
                    finish()
                }
                is SignUpViewModel.SignUpResult.Error -> {
                    Util.showToast(this@SignUpActivity, "Error: ${result.message}")
                }
            }
        })
    }
    
    private fun validateData(
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = "Invalid e-mail."
            return false
        }
        if (password.length < 6) {
            binding.edtPassword.error = "Password must be at least 6 characters."
            return false
        }
        if (password != confirmPassword) {
            binding.edtConfirmPassword.error = "Passwords do not match."
            return false
        }
        return true
    }
}
