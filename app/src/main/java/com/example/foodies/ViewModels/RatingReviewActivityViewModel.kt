package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.UserModel
import com.example.foodies.Respository.DataRepository

class RatingReviewActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun saveReview(userReview: Review, userId: String): MutableLiveData<Boolean>? {
        return dataRepo?.saveReview(userReview,userId)
    }
    fun getUserInfo(userId:String): MutableLiveData<UserModel>? {
        return dataRepo?.getUserInfo(userId)
    }

}