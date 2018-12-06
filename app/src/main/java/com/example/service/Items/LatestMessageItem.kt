package com.example.service.Items

import com.example.service.ChatMessage
import com.example.service.R
import com.example.service.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latest_message_row.view.*

class LatestMessageItem(val chatMessage: ChatMessage) : Item<ViewHolder>() {

    var contactUser: User? = null

    override fun getLayout(): Int {
        return R.layout.latest_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val lastSenderId: String
        if (chatMessage.currentId == FirebaseAuth.getInstance().uid)
            lastSenderId = chatMessage.currentId
        else
            lastSenderId = chatMessage.contactId
        val contactRef = FirebaseDatabase.getInstance().getReference("/users/${chatMessage.contactId}")
        contactRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                contactUser = p0.getValue(User::class.java)
                viewHolder.itemView.textView_contact_username.text = contactUser?.userName
                Picasso.get().load(contactUser?.avatarUrl).into(viewHolder.itemView.useravatar_latestmessage_circleimageView)
            }

        })
        val lastSenderRef = FirebaseDatabase.getInstance().getReference("/users/$lastSenderId")

        lastSenderRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(User::class.java)
                Picasso.get().load(user?.avatarUrl).into(viewHolder.itemView.lastSenderAvatar_circleimageview)

                viewHolder.itemView.textView_latest_message.text = chatMessage.text
            }

        })


    }
}