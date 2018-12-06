package com.example.service.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.example.service.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Timer().schedule(1000) {
            userIsLoggedInVerification()

        }
    }

    private fun userIsLoggedInVerification() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intentLoginActivity = Intent(this, LoginActivity::class.java)
            intentLoginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentLoginActivity)
            finish()
        } else {
            val intentMainActivity = Intent(this, ChatroomsActivity::class.java)
            startActivity(intentMainActivity)
            finish()
        }
    }
}

