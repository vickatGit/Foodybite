package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Respository.DataRepository

class FavoritesFragmentViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getFavourites(userId: String, favouriteIds: ArrayList<String>): MutableLiveData<List<BusinessDetailModel>>? {
        return dataRepo?.getFavourites(userId,favouriteIds)
    }
    fun getUserFavouriteBusineses(userId: String): MutableLiveData<List<String>>? {
        return dataRepo?.getUserFavouriteBusinesesIds(userId)
    }
}