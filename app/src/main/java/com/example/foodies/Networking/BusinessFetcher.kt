package com.example.foodies.Networking

import com.example.foodies.Models.BusinessModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface BusinessFetcher {
    @GET("businesses/search")
    fun search(
        @Header("Authorization") authHeader: String,
        @Query("term") searchQuery:String,
        @Query("location") location:String
    ): Call<Any>

    @GET("businesses/search")
    fun getPopularRestaurants(
        @Header("Authorization") authHeader: String,
        @Query("location") location:String,
        @Query("categories") category: String,
        @Query("sort_by") sorter:String,
        @Query("attributes") filter: Array<String>
    ):Call<BusinessModel>
}