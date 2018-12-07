package com.example.service

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String = "", val avatarUrl: String = "", val userName: String = "",val messaging_token:String="") : Parcelable