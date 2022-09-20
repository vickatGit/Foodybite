package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.Businesse
import com.example.foodies.Respository.DataRepository

class CategoryBusinessViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getCategoryData(category: String?): MutableLiveData<List<Businesse>>? {
        return dataRepo?.getCategoryData(category)
    }

}