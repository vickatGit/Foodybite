package com.example.foodies.Models.BusinessViewerModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessDetailModel(
    val alias: String,
    val categories: List<Category>?,
    val coordinates: Coordinates,
    val display_phone: String,
    val hours: List<Hour>?,
    val id: String,
    val image_url: String,
    val is_claimed: Boolean,
    val is_closed: Boolean,
    val location: Location,
    val name: String,
    val phone: String,
    val photos: List<String>?,
    val price: String?,
    val rating: Double,
    val review_count: Int,
    val special_hours: List<SpecialHour>?,
    val transactions: List<String>?,
    val url: String
) : Parcelable