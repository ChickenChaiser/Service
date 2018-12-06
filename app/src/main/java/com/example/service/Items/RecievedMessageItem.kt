package com.example.service.Items

import com.example.service.R
import com.example.service.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recieve_message_row.view.*

class RecievedMessageItem(val user: User, val text: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.recieve_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.recieve_message.text = text

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.contactUser_avatar)
    }
}