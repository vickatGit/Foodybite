package com.example.foodies.Models.BusinessViewerModel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Hour(
    val hours_type: String,
    val is_open_now: Boolean,
    val `open`: List<Open>
) : Parcelable