package com.example.foodies.Models.BusinessReviewModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessReviewsModel(
    val possible_languages: List<String>?,
    val reviews: List<Review>,
    val total: Int
) : Parcelable