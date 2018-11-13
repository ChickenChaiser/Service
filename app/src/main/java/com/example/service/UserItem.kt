package com.example.service

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder

class UserItem: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.user_row_new_chat
    }

    override fun bind(viewHolder: ViewHolder, position: Int){
    }
}