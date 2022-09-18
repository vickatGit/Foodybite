package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.BusinessReviewsAdapter
import com.example.foodies.Models.BusinessReviewModel.Review

class AllReviewsActivity : AppCompatActivity() {
    private var reviews=ArrayList<Review>(1)
    private lateinit var reviewsRecycler:RecyclerView
    private lateinit var reviewsAdapter: BusinessReviewsAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_reviews)
        reviews.addAll(intent.getParcelableArrayListExtra<Review>(BusinessActivity.REVIEWS_BRIDGE)!!)
        initialise()
        reviewsRecycler.layoutManager=LinearLayoutManager(this)
        reviewsAdapter= BusinessReviewsAdapter(reviews,false)
        reviewsRecycler.adapter=reviewsAdapter

    }

    private fun initialise() {
        reviewsRecycler=findViewById(R.id.reviews)
    }
}