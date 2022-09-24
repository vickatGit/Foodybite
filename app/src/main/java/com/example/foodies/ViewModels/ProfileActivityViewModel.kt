package com.example.foodies.ViewModels

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Models.UserModel
import com.example.foodies.Respository.DataRepository

class ProfileActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun storeUserProfilePic(userId: String, bitmap: Bitmap?,filePath:Uri?) {
        dataRepo?.storeUserProfilePic(userId,bitmap,filePath)
    }

    fun getUserProfilePic(userId: String): MutableLiveData<Uri?>? {
        return dataRepo?.getUserProfilePic(userId)
    }

    fun getUserReviewedBusinesses(userId: String): MutableLiveData<List<BusinessDetailModel>>? {
         return dataRepo?.getUserReviewedBusinesses(userId)

    }

    fun getUser(userId: String): MutableLiveData<UserModel>? {
        return dataRepo?.getUser(userId)

    }

    fun followUser(userId: String, friendId: String?) {
        dataRepo?.followUser(userId,friendId!!)
    }

    fun unfollowUser(userId: String, friendId: String?) {
        dataRepo?.unfollowUser(userId,friendId!!)
    }
    fun isUserFollowing(userId: String, friendId: String?): MutableLiveData<Boolean>? {
        return dataRepo?.isUserFollowing(userId,friendId!!)
    }
    fun getFollowerIds(userId: String): MutableLiveData<List<String>>? {
        return dataRepo?.getFollowersIds(userId)
    }

    fun getUserFollowing(userId: String): MutableLiveData<List<String>>? {
        return dataRepo?.getUserFollowing(userId)
    }

}