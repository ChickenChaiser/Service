package com.example.service.Activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

import kotlinx.android.synthetic.main.activity_main.*
import android.view.View
import android.widget.LinearLayout
import com.example.service.ChatMessage
import com.example.service.Items.LatestMessageItem
import com.example.service.Items.UserItem
import com.example.service.R
import com.example.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlin.collections.HashMap

class ChatroomsActivity : AppCompatActivity() {


    companion object {
        var currentUser: User? = null
    }

    val adapter = GroupAdapter<ViewHolder>()
    val latestMessageMap = HashMap<String, ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getCurrentUser()

        val rv = findViewById<RecyclerView>(R.id.recyclerview_latest_messages)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        recyclerview_latest_messages.adapter = adapter
        recyclerview_latest_messages.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        listenForLatestMesages()

        adapter.setOnItemClickListener { item, view ->

            val row = item as LatestMessageItem
            val intentChatActivity = Intent(this, ChatActivity::class.java)

            intentChatActivity.putExtra(StartNewChatActivity.USER_KEY, row.contactUser)
            startActivity(intentChatActivity)
        }

        fab_new_chat.setOnClickListener {
            val intentNewChatActivity = Intent(this, StartNewChatActivity::class.java)
            startActivity(intentNewChatActivity)
        }

        logOut_button.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intentLoginActivity = Intent(this, LoginActivity::class.java)
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

        user_avatar.setOnClickListener {
            val intentSettingsActivity = Intent(this, SettingsActivity::class.java)
            startActivity(intentSettingsActivity)
        }
    }

    private fun getCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                drawerlayout_userName.text = currentUser?.userName
                Picasso.get().load(currentUser?.avatarUrl).into(drawerlayout.user_avatar)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun listenForLatestMesages() {
        val currentId = FirebaseAuth.getInstance().uid
        //val contactUser = intent.getParcelableExtra<User>(StartNewChatActivity.USER_KEY)
        //val contactId = contactUser.uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$currentId").orderByChild("timestamp")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshLatestMessageRow()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshLatestMessageRow()
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })
    }

    private fun refreshLatestMessageRow() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageItem(it))
        }
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
