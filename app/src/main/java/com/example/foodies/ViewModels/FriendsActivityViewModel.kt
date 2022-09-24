package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.UserModel
import com.example.foodies.Respository.DataRepository

class FriendsActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getFriends(friendsListIds: ArrayList<String>): MutableLiveData<List<UserModel>>? {
        return dataRepo?.getFriends(friendsListIds)
    }
    fun followUser(userId: String, friendId: String?) {
        dataRepo?.followUser(userId,friendId!!)
    }

    fun unfollowUser(userId: String, friendId: String?) {
        dataRepo?.unfollowUser(userId,friendId!!)
    }

}