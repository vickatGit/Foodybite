package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(val username:String, var email:String, var password:String?, var userRef:String? ,var userImage:String?) :
    Parcelable
