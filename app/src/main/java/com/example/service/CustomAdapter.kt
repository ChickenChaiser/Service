package com.example.service

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.hdodenhof.circleimageview.CircleImageView

class CustomAdapter/*(val userList: ArrayList<UserItem>): RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.user_row_new_chat, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
       // p0?.avatarUrl?.
        p0?.txtName?.text = userList[p1].userName

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val avatarUrl=itemView.findViewById<CircleImageView>(R.id.user_row_circleimageView)
        val txtName = itemView.findViewById<TextView>(R.id.user_row_userName)

    }

}*/