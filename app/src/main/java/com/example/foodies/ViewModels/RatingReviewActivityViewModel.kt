package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Respository.DataRepository

class RatingReviewActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun saveReview(experience: HashMap<String, String?>, userId: String): MutableLiveData<Boolean>? {
        return dataRepo?.saveReview(experience,userId)
    }

}