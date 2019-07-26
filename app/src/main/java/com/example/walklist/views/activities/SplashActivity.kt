package com.example.walklist.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.walklist.R
import com.example.walklist.controllers.UserController
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(500) {

            var intent = Intent(this@SplashActivity, LoginActivity::class.java)
            if (UserController.isLoggedIn(this@SplashActivity)) { // Update with is logged in
                intent = Intent(this@SplashActivity, MainActivity::class.java)
            }
            startActivity(intent)

            // Task to finish the splash activity
            Timer().schedule(500) {
                finish()
            }
        }
    }
}
