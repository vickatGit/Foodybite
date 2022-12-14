package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Center(
    val latitude: Double,
    val longitude: Double
) : Parcelable