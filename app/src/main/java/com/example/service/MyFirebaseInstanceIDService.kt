package com.example.service

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceIdService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.iid.FirebaseInstanceId



class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onTokenRefresh() {
        // Get updated InstanceID token.
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: " + refreshedToken!!)

        sendRegistrationToServer(refreshedToken)
    }


    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token!!)
        val reference = FirebaseDatabase.getInstance().reference
        reference.child("/users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("messaging_token")
                .setValue(token)
    }

    companion object {
        private val TAG = "MyFirebaseIIDService"
    }
}
