package com.example.service

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String = "", val avatarUrl: String = "", val userName: String = "") : Parcelable