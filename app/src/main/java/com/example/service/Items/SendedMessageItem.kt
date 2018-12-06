package com.example.service.Items

import com.example.service.R
import com.example.service.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.send_message_row.view.*

class SendedMessageItem(val user: User, val text: String) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.send_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.send_message.text = text

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.currentUser_avatar)
    }
}