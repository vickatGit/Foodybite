package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BusinessModel(
    val businesses: List<Businesse>,
    val region: Region,
    val total: Int
) : Parcelable