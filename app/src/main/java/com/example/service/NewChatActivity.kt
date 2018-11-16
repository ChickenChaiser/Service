package com.example.service

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.LinearLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*

class NewChatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)
        val rv = findViewById<RecyclerView>(R.id.recyclerview_newchat_userslist)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val users = ArrayList<User>()
        //users.add(UserItem("kek"))
        //users.add(UserItem("sds"))
        //val customAdapter=CustomAdapter(users)
        //val adapter = GroupAdapter<ViewHolder>()
        //rv.adapter=customAdapter

        getUsers()

    }

    fun getUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user= it.getValue(User::class.java)
                    if (user!=null)
                    adapter.add(UserItem(user))
                    Log.d("showing user","$it")
                }
                recyclerview_newchat_userslist.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
    }
}
