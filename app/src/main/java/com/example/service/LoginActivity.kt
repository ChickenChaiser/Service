package com.example.service

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val intentMainActivity = Intent(this,MainActivity::class.java)
        login_button.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()
            if (email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Enter Email or password or both",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener {
                        if(!it.isSuccessful) return@addOnCompleteListener
                        Log.d("LoginActivity","Login successfull")
                        intentMainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intentMainActivity)
                    }
                    .addOnFailureListener {
                        Log.d("LoginActivity","Login failed")
                        Toast.makeText(this,"Failed to login: ${it.message}", Toast.LENGTH_SHORT).show()
                    }

        }
        val intentRegistrationActivity = Intent(this,RegistrationActivity::class.java)
        login_textview.setOnClickListener {
            startActivity(intentRegistrationActivity)
        }
    }
}
