package app.kotlin.devpbleite.inotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import app.kotlin.devpbleite.inotes.databinding.ActivitySplashBinding
import app.kotlin.devpbleite.inotes.view.LoginActivity
import app.kotlin.devpbleite.inotes.view.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAppropriateScreen()
        }, 1000)
    }
    
    private fun navigateToAppropriateScreen() {
        val intent = when (FirebaseAuth.getInstance().currentUser) {
            null -> Intent(this@SplashActivity, LoginActivity::class.java)
            else -> Intent(this@SplashActivity, MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
