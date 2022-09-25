package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.UserModel
import com.example.foodies.Respository.DataRepository

class FindFriendFragmentViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }


}