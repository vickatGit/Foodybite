package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.Businesse
import com.example.foodies.Respository.DataRepository

class HomeFragmentViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getPopularRestaurants(): MutableLiveData<List<Businesse>>? {
        return dataRepo?.getFamousRestaurants()
    }

    fun search(query: String): MutableLiveData<List<Businesse>>? {

        return dataRepo?.search(query)

    }

    fun getPubs(): MutableLiveData<List<Businesse>>? {
        return dataRepo?.getPubs()
    }

}