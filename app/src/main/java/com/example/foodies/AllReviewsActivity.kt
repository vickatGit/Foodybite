package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.BusinessReviewsAdapter
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.ViewModels.BusinessActivityViewModel

class AllReviewsActivity : AppCompatActivity() {
    private var reviews=ArrayList<Review>(1)
    private lateinit var reviewsRecycler:RecyclerView
    private lateinit var reviewsAdapter: BusinessReviewsAdapter
    private lateinit var userId:String
    private lateinit var viewModel:BusinessActivityViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_reviews)
        reviews.addAll(intent.getParcelableArrayListExtra<Review>(BusinessActivity.REVIEWS_BRIDGE)!!)
        initialise()
        userId=intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        reviewsRecycler.layoutManager=LinearLayoutManager(this)
        reviewsAdapter= BusinessReviewsAdapter(reviews, false, userId, viewModel,this)
        reviewsRecycler.adapter=reviewsAdapter

    }

    private fun initialise() {
        reviewsRecycler=findViewById(R.id.reviews)
    }
}