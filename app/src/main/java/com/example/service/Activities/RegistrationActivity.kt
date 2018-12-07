package com.example.service.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.service.R
import com.example.service.R.drawable.camera150
import com.example.service.R.id.register_username_edittext
import com.example.service.User
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.net.URL
import java.util.*
import com.google.firebase.iid.FirebaseInstanceId


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
            val intentPic = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intentPic, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            avatarUri = data.data
            register_select_image_imageview.setImageURI(avatarUri)
        }
    }

    private fun Register() {
        val password = register_password_edittext.text.toString()
        val email = register_email_edittext.text.toString()
        val intentMainActivity = Intent(this, ChatroomsActivity::class.java)
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter Email or password or both", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (!it.isSuccessful) return@addOnCompleteListener
                    Log.d("latest", "Succesfully created new user: ${it.result!!.user.uid}")
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
        if (avatarUri == null) {
            val url = "https://firebasestorage.googleapis.com/v0/b/service-1d160.appspot.com/o/images%2Fcamera150.png?alt=media&token=afc8ea8d-bdf9-4c33-a10b-6e469bcff6c2"
            saveUserToFirebaseDatabase(url)
        } else {
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

    }

    private fun saveUserToFirebaseDatabase(avatarUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val token = FirebaseInstanceId.getInstance().getToken()
        val user = User(uid, avatarUrl, register_username_edittext.text.toString(),token!!)


        ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "User added to database")
                }
    }
}
