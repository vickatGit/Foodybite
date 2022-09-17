package com.example.foodies.Models.BusinessReviewModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review(
    val id: String,
    val rating: Int,
    val text: String,
    val time_created: String,
    val url: String,
    val user: User
) : Parcelable