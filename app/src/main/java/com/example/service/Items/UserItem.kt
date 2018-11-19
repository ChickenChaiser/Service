package com.example.service.Items

import com.example.service.R
import com.example.service.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_new_chat_item.view.*

class UserItem(val user: User) : Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.user_row_userName.text=user.userName

        Picasso.get().load(user.avatarUrl).into(viewHolder.itemView.user_row_circleimageView )
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_chat_item
    }

}