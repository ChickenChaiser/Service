package com.example.service.Items

import com.example.service.R
import com.example.service.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recieved_message_row.view.*
import kotlinx.android.synthetic.main.user_row_new_chat_item.view.*

class RecievedMessageItem (val user: User, val text:String) : Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.recieved_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.recieved_message.text =text

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.currentUser_avatar )
    }
}