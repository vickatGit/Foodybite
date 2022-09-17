package com.example.foodies.Models.BusinessReviewModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val image_url: String,
    val name: String,
    val profile_url: String
) : Parcelable