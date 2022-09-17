package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Region(
    val center: Center
) : Parcelable