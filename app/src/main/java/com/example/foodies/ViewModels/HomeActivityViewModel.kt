package com.example.foodies.ViewModels

import androidx.lifecycle.ViewModel
import com.example.foodies.Respository.DataRepository

class HomeActivityViewModel: ViewModel() {
    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }

}