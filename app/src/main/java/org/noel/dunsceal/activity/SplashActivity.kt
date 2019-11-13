package org.noel.dunsceal.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import org.noel.dunsceal.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeFullScreen()
        setContentView(R.layout.splash_activity)
// Using a handler to delay loading the DunMainActivity
        Handler().postDelayed({
            // Start activity
            startActivity(Intent(this, LoginActivity::class.java))
            // Animate the loading of new activity
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            // Close this activity
            finish()
        }, 2000)
    }

    private fun makeFullScreen() {
        // Remove Title
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        // Make Fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        // Hide the toolbar
        supportActionBar?.hide()
    }
}