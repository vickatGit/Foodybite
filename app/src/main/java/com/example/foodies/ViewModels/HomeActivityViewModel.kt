package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Respository.DataRepository

class HomeActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getUserFavouriteBusineses(userId: String): MutableLiveData<List<String>>? {
        return dataRepo?.getUserFavouriteBusinesesIds(userId)
    }
    fun getUserFollowing(userId: String): MutableLiveData<List<String>>? {
        return dataRepo?.getUserFollowing(userId)
    }

}