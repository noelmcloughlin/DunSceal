package ie.noel.dunsceal.views.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import ie.noel.dunsceal.R

class SplashView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.splash_activity)
        // Using a handler to delay loading the DunMainActivity
        Handler().postDelayed({
            // Start activity
            startActivity(Intent(this, LoginView::class.java))
            // Animate the loading of new activity
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            // Close this activity
            finish()
        }, 1000)
    }

    private fun makeFullScreen() {
        // Remove Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Make Fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        // Hide the toolbar
        supportActionBar?.hide()
    }
}