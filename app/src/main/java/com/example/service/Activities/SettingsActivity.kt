package com.example.service.Activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.service.R
import com.example.service.R.string.userdata_update
import com.example.service.User
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_settings.*
import java.util.*

class SettingsActivity : AppCompatActivity() {

    var avatarUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        getCurrentUser()

        user_avatar_settings.setOnClickListener {
            val intentPic = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intentPic, 0)
        }

        save_changes_button.setOnClickListener {

            if (avatarUri != null)
                uploadImageToFirebaseStorage()

            val newUserName=editUsername_settings_editText.text.toString()
            if (newUserName.isNotEmpty())
                changeUserName(newUserName)

            val currentPassword = currentPassword_settings_editText.text.toString()
            val newPassword = newPassword_settings_editText.text.toString()

            if (currentPassword.isNotEmpty() || newPassword.isNotEmpty())
                changeUserPassword(currentPassword,newPassword)

            Toast.makeText(this,R.string.userdata_update, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            avatarUri = data.data
            user_avatar_settings.setImageURI(avatarUri)
        }
    }

    private fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                ChatroomsActivity.currentUser = p0.getValue(User::class.java)
                editUsername_settings_editText.hint = ChatroomsActivity.currentUser?.userName
                Picasso.get().load(ChatroomsActivity.currentUser?.avatarUrl).into(user_avatar_settings)

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun changeUserName(newUserName:String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference().child("/users/$uid")
        ref.child("userName").setValue(newUserName)
    }

    private fun changeUserAvatar(avatarUrl: String) {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference().child("/users/$uid")
        ref.child("avatarUrl").setValue(avatarUrl)
    }

    private fun changeUserPassword(currentPassword:String, newPassword:String) {
        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email
        val credential = EmailAuthProvider
                .getCredential(email.toString(), currentPassword)
        user!!.reauthenticate(credential)
                .addOnSuccessListener {
                    user!!.updatePassword(newPassword)
                }
                .addOnFailureListener {
                    Toast.makeText(this,R.string.password_change_failed,Toast.LENGTH_SHORT).show()
                }


    }

    private fun uploadImageToFirebaseStorage(){

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(avatarUri!!)
                .addOnSuccessListener {
                    Log.d("RegistrationActivity", "Successfully uploaded: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("RegistrationActivity", "File location $it")
                        changeUserAvatar(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("RegistrationActivity", "Failed to uplad image")
                }
    }
}
