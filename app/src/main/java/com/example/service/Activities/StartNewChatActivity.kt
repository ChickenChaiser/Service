package com.example.service.Activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import com.example.service.Items.UserItem
import com.example.service.R
import com.example.service.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_new_chat.*

class StartNewChatActivity : AppCompatActivity() {

    companion object {
        val USER_KEY = "USER_KEY"
    }

    var sortedUsers = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_chat)

        val rv = findViewById<RecyclerView>(R.id.recyclerview_newchat_userslist)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        getUsers()

    }

    fun getUsers(){
        val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("userName")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                p0.children.forEach{
                    val user= it.getValue(User::class.java)
                    if (user!=null)
                    //adapter.add(UserItem(user))
                        sortedUsers.add(user)
                }
                sortedUsers.sortBy { user -> user.userName.toUpperCase() }
                sortedUsers.forEach {
                    adapter.add(UserItem(it))
                }
                adapter.setOnItemClickListener { item, view ->
                    val userItem=item as UserItem
                    val intentChatActivity = Intent(view.context, ChatActivity::class.java)
                    intentChatActivity.putExtra(USER_KEY,userItem.user)
                    startActivity(intentChatActivity)

                    finish()
                }
                recyclerview_newchat_userslist.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
     //   val adapter = GroupAdapter<ViewHolder>()
        /*sortedUsers?.sortBy { user -> user.userName.toString().toUpperCase() }
        sortedUsers?.forEach {
            adapter.add(UserItem(it))
        }*/
        /*adapter.setOnItemClickListener { item, view ->
            val userItem=item as UserItem
            val intentChatActivity = Intent(view.context, ChatActivity::class.java)
            intentChatActivity.putExtra(USER_KEY,userItem.user)
            startActivity(intentChatActivity)

            finish()
        }
        recyclerview_newchat_userslist.adapter = adapter*/
                //.OrderBy(person => person.Age).ThenBy(person => person.Name).ToList()
    }

    private fun sortUsers(sortedUsers:ArrayList<User>){

    }
}
