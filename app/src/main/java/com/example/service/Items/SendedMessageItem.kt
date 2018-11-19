package com.example.service.Items

import com.example.service.R
import com.example.service.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recieved_message_row.view.*
import kotlinx.android.synthetic.main.sended_message_row.view.*
import kotlinx.android.synthetic.main.user_row_new_chat_item.view.*

class SendedMessageItem (val user: User,val text:String): Item<ViewHolder>(){
    override fun getLayout(): Int {
        return R.layout.sended_message_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.sended_message.text = text

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.contactUser_avatar )
    }
}