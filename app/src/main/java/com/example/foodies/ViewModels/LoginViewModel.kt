package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Respository.DataRepository

class LoginViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun isUserExist(email: String, password: String): MutableLiveData<String>? {
        return dataRepo?.isUserExist(email,password)
    }
}