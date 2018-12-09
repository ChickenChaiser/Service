package com.example.service.Items

import android.content.Context
import android.widget.Toast
import com.example.service.Activities.ChatActivity
import com.example.service.ChatMessage
import com.example.service.R
import com.example.service.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.send_message_row.view.*

class SendedMessageItem(val user: User ,val chatMessage: ChatMessage,val context: Context,val reflist:ArrayList<String>) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.send_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.send_message.text = chatMessage.text

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.currentUser_avatar)
    }

    fun deleteMessage(){
        reflist.forEach {
        FirebaseDatabase.getInstance().getReferenceFromUrl(it)
                .removeValue()

        FirebaseDatabase.getInstance().getReferenceFromUrl(it)
                .removeValue()
        FirebaseDatabase.getInstance().getReferenceFromUrl(it)
                .removeValue()

        FirebaseDatabase.getInstance().getReferenceFromUrl(it)
                .removeValue().addOnSuccessListener {
                    Toast.makeText(context,"Message deleted",Toast.LENGTH_SHORT)
                }
        }
    }
}