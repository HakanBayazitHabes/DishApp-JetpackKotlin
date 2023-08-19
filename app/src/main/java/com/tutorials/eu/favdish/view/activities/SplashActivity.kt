package com.tutorials.eu.favdish.view.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.tutorials.eu.favdish.R
import com.tutorials.eu.favdish.databinding.ActivitySplashBinding

// TODO Step 1: Create a new activity as Splash Screen.
// START
/**
 * A Splash Screen
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO Step 5: Access the XML layout file using the ViewBiding.
        // START
        val splashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        // END

        // TODO Step 6: Update the content view using the ViewBinding
        // START
        setContentView(splashBinding.root)
        // END

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(android.view.WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        var splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash)
        splashBinding.tvAppName.animation = splashAnimation


        splashAnimation.setAnimationListener(object: Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }
        })

    }
}
// END