package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Businesse(
    val alias: String?,
    val categories: List<Category>?,
    val coordinates: Coordinates?,
    val distance: Double?,
    val id: String?,
    val image_url: String?,
    val is_closed: Boolean?,
    val location: Location?,
    val name: String?,
    val phone: String?,
    val price: String?,
    val rating: Float?,
    val review_count: Int?,
    val transactions: List<String>?,
    val url: String?
) : Parcelable