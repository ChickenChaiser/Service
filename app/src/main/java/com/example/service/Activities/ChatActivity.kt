package com.example.service.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.service.ChatMessage
import com.example.service.Items.SendedMessageItem
import com.example.service.Items.RecievedMessageItem
import com.example.service.R
import com.example.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_chat.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class ChatActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val contactUser = intent.getParcelableExtra<User>(StartNewChatActivity.USER_KEY)

        supportActionBar?.title = contactUser?.userName

        val rv = findViewById<RecyclerView>(R.id.recyclerview_chat)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        recyclerview_chat.adapter = adapter


        showMessages()

        button_send_message.setOnClickListener {
            sendMessage()
        }
    }

    fun sendMessage() {

        val message = editText_write_message.text.toString()
        val currentId = FirebaseAuth.getInstance().uid
        val contactUser = intent.getParcelableExtra<User>(StartNewChatActivity.USER_KEY)
        val contactId = contactUser.uid
        if (currentId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$currentId/$contactId").push()
        val contactRef = FirebaseDatabase.getInstance().getReference("/user-messages/$contactId/$currentId").push()
        val chatMessage = ChatMessage(ref.key!!, message, contactId, currentId, System.currentTimeMillis() / 1000)

        ref.setValue(chatMessage)
                .addOnSuccessListener {
                    editText_write_message.text.clear()
                    recyclerview_chat.scrollToPosition(adapter.itemCount-1)
                }
        contactRef.setValue(chatMessage)


        val latestMessageRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$currentId/$contactId")
        latestMessageRef.setValue(chatMessage)

        val latestMessageContactRef=FirebaseDatabase.getInstance().getReference("/latest-messages/$contactId/$currentId")
        latestMessageContactRef.setValue(chatMessage)
    }

    fun showMessages() {
        val currentId = FirebaseAuth.getInstance().uid
        val contactUser = intent.getParcelableExtra<User>(StartNewChatActivity.USER_KEY)
        val contactId = contactUser?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$currentId/$contactId")
        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)
                if (chatMessage != null) {
                    if (chatMessage.currentId == FirebaseAuth.getInstance().uid) {
                        val currentUser=ChatroomsActivity.currentUser?:return
                        adapter.add(SendedMessageItem(currentUser,chatMessage.text))
                    }
                    else {
                        adapter.add(RecievedMessageItem(contactUser!!,chatMessage.text))
                    }
                }
                recyclerview_chat.scrollToPosition(adapter.itemCount-1)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })


        recyclerview_chat.adapter = adapter
    }
}
