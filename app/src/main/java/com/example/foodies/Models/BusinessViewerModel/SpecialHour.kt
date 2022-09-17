package com.example.foodies.Models.BusinessViewerModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecialHour(
    val date: String,
    val end: String,
    val is_closed: Boolean,
    val is_overnight: Boolean,
    val start: String
) : Parcelable