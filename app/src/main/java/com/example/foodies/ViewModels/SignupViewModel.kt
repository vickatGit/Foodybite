package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.UserModel
import com.example.foodies.Respository.DataRepository

class SignupViewModel: ViewModel() {

    private var dataRepo:DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun registerUser(user: UserModel): MutableLiveData<String>? {
        return dataRepo?.registerUser(user)
    }

    fun isEmailAlreadyExists(email: String): MutableLiveData<Boolean>? {
        return dataRepo?.isEmailAlreayExists(email)
    }

}