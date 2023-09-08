package app.kotlin.devpbleite.inotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    
        fun delayedStartActivity() {
            val scope = CoroutineScope(Dispatchers.Main)
        
            scope.launch {
                delay(1000L)
    
                when (FirebaseAuth.getInstance().currentUser) {
                    null -> {
                        val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else -> {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        startActivity(intent)
                    }
                }
                finish()
            }
        }
    }
}