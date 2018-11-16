package com.example.service

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    var avatarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)


        registerButton.setOnClickListener {
            Register()

        }

        val intentLoginActivity = Intent(this, LoginActivity::class.java)
        register_textview.setOnClickListener {
            intentLoginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intentLoginActivity)
        }

        register_select_image_imageview.setOnClickListener {
            val intentPic = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intentPic, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            avatarUri = data.data
            //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, avatarUri)
            register_select_image_imageview.setImageURI(avatarUri)
            //val bitmapDrawable=BitmapDrawable(bitmap)
            //register_select_image_button.setBackgroundDrawable(bitmapDrawable)
        }
    }

    private fun Register() {
        val password = register_password_edittext.text.toString()
        val email = register_email_edittext.text.toString()
        val intentMainActivity = Intent(this, MainActivity::class.java)
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter Email or password or both", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("RegistrationActivity", "Succesfully created new user: ${it.result!!.user.uid}")
                    uploadImageToFirebaseStorage()
                    intentMainActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intentMainActivity)

                }
                .addOnFailureListener {
                    Log.d("RegistrationActivity", "Failed to create user: ${it.message}")
                    Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
                }
    }

    private fun uploadImageToFirebaseStorage() {
        if (avatarUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(avatarUri!!)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "Successfully uploaded: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegistrationActivity", "File location $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("RegistrationActivity", "Failed to uplad image")
                }
    }

    private fun saveUserToFirebaseDatabase(avatarUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, avatarUrl, register_username_edittext.text.toString())

        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "User added to database")
                }
    }

}
