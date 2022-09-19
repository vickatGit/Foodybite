package com.example.foodies.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foodies.Models.BusinessReviewModel.BusinessReviewsModel
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Respository.DataRepository

class BusinessActivityViewModel: ViewModel() {

    private var dataRepo: DataRepository?=null
    init {
        dataRepo= DataRepository.getInstance()
    }
    fun getBusiness(id: String?): MutableLiveData<BusinessDetailModel>? {
        return dataRepo?.getBusiness(id)
    }

    fun getBusinessReviews(id: String?): MutableLiveData<BusinessReviewsModel>? {
        return dataRepo?.getBusinessReviews(id)

    }

    fun getBusinessReviewsFromDatabase(id: String?): MutableLiveData<List<Review>>? {
        return dataRepo?.getBusinessReviewsFromDatabase(id)
    }

}