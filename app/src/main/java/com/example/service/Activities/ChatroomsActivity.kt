package com.example.service.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import com.example.service.R
import com.example.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.view.*
import java.util.*

class ChatroomsActivity : AppCompatActivity() {


    companion object {
        var currentUser: User?=null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentUser()



        fab_new_chat.setOnClickListener {
            val intentNewChatActivity = Intent(this, StartNewChatActivity::class.java)
            startActivity(intentNewChatActivity)
        }
        logOut_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intentLoginActivity =Intent(this, LoginActivity::class.java)
            intentLoginActivity.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intentLoginActivity)
        }

            //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            // .setAction("Action", null).show()

        var mDrawer = findViewById<View>(R.id.drawerlayout) as FlowingDrawer
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL)
        mDrawer.setOnDrawerStateChangeListener(object : ElasticDrawer.OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    Log.i("ChatroomsActivityMsg", "Drawer STATE_CLOSED")
                }
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {
                Log.i("ChatroomsActivityMsg", "openRatio=$openRatio ,offsetPixels=$offsetPixels")
            }

        })
    }

    private fun getCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                currentUser=p0.getValue(User::class.java)

                Picasso.get().load(currentUser?.avatarUrl).into(drawerlayout.user_avatar )
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    /*private fun changeAvatarInFirebaseStorage() {
        if (avatarUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")
        ref.putFile(avatarUri!!)
                .addOnSuccessListener {
                    Log.d("ChatroomsActivityMsg", "Successfully uploaded: ${it.metadata?.path}")

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("ChatroomsActivityMsg", "File location $it")
                        saveUserToFirebaseDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("ChatroomsActivityMsg", "Failed to uplad image")
                }
    }*/

}
