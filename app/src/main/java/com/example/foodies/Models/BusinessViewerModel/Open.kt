package com.example.foodies.Models.BusinessViewerModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Open(
    val day: Int,
    val end: String,
    val is_overnight: Boolean,
    val start: String
) : Parcelable