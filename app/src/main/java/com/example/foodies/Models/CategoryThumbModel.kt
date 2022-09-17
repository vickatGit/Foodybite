package com.example.foodies.Models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryThumbModel(val categoryName:String,val categoryImage:String) : Parcelable
