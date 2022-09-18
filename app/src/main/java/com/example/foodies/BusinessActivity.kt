package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.Adapters.BusinessPhotoGalleryAdapter
import com.example.foodies.Adapters.BusinessReviewsAdapter
import com.example.foodies.Adapters.PopularRestaurantsAdapter
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Models.BusinessViewerModel.Hour
import com.example.foodies.Models.Businesse
import com.example.foodies.ViewModels.BusinessActivityViewModel
import com.google.gson.Gson

class BusinessActivity : AppCompatActivity() {
    private lateinit var businessPoster: ImageView
    private lateinit var businessName: TextView
    private lateinit var businessAddress: TextView
    private lateinit var businessStatus: TextView
    private lateinit var businessTiming: TextView
    private lateinit var businessPhotoGalleryRecycler: RecyclerView
    private lateinit var businessPhotoGalleryAdapter: BusinessPhotoGalleryAdapter
    private lateinit var businessReviewsRecycler: RecyclerView
    private lateinit var seeAllReviews: TextView
    private lateinit var businessRating: TextView
    private var businessImages = ArrayList<String>(1)
    private var businessReviews = ArrayList<Review>(1)
    private lateinit var businessReviewsAdapter: BusinessReviewsAdapter
    private lateinit var viewModel: BusinessActivityViewModel
    private lateinit var businessDetails: BusinessDetailModel

    companion object {
        val REVIEWS_BRIDGE = "reviews_passer_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)
        viewModel = ViewModelProvider(this).get(BusinessActivityViewModel::class.java)
        val business =
            intent.getParcelableExtra<Businesse>(PopularRestaurantsAdapter.BUSINESS_BRIDGE)
        initialise()
        businessPhotoGalleryRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        businessReviewsRecycler.layoutManager = LinearLayoutManager(this)
        businessReviewsAdapter = BusinessReviewsAdapter(businessReviews, true)
        businessReviewsRecycler.adapter = businessReviewsAdapter
        businessPhotoGalleryAdapter = BusinessPhotoGalleryAdapter(businessImages)
        businessPhotoGalleryRecycler.adapter = businessPhotoGalleryAdapter
        viewModel.getBusiness(business?.id)?.observe(this, Observer {
            if (it != null) {
                businessDetails = it
                businessImages.clear()
                businessImages.addAll(businessDetails.photos)
                businessPhotoGalleryAdapter.notifyDataSetChanged()
                businessRating.text = businessDetails.rating.toString()
                businessAddress.text = businessDetails.location.display_address?.get(0)
                if (businessDetails.hours != null) {
                    Log.d("TAG", "onCreate: business opens at" + Gson().toJson(businessDetails.hours.get(0)))
                    businessTiming.text = businessTimingProcessor(businessDetails.hours.get(0))
                }else{ businessTiming.text="N/A"}
            }
        })
        viewModel.getBusinessReviews(business?.id)?.observe(this, Observer {
            if (it != null) {
                Log.d("TAG", "onCreate: reviews $it")
                businessReviews.clear()
                businessReviews.addAll(it.reviews)

                if (it.reviews.size > 0) seeAllReviews.visibility = View.VISIBLE
                businessReviewsAdapter.notifyDataSetChanged()
            }
        })
        Glide.with(this).load(business?.image_url).into(businessPoster)
        businessName.text = business?.name
        if (business?.is_closed == true) businessStatus.text = "Open Now" else businessStatus.text =
            "closed"
        seeAllReviews.setOnClickListener {
            val intent = Intent(this, AllReviewsActivity::class.java)
            intent.putExtra(REVIEWS_BRIDGE, businessReviews)
            startActivity(intent)
        }
    }

    private fun businessTimingProcessor(hour: Hour): String {

        val start = hour.open.get(0).start
        var successor = ""
        if (start.substring(2, 4).toInt() % 60 == 0) successor = "0"
        val startHour =
            (start.substring(0, 2).toInt() % 12).toString() + ":" + (start.substring(2, 4)
                .toInt() % 60).toString() + "$successor am"

        val end = hour.open.get(0).end
        successor = ""
        if (end.substring(2, 4).toInt() % 60 == 0) successor = "0"
        val endHour = (end.substring(0, 2).toInt() % 12).toString() + ":" + (end.substring(2, 4)
            .toInt() % 60).toString() + "$successor pm"
        return startHour + " to " + endHour
    }

    private fun initialise() {
        businessPoster = findViewById(R.id.business_poster)
        businessName = findViewById(R.id.business_name)
        businessRating = findViewById(R.id.business_rating)
        businessAddress = findViewById(R.id.business_address)
        businessStatus = findViewById(R.id.is_open)
        businessTiming = findViewById(R.id.business_timing)
        businessPhotoGalleryRecycler = findViewById(R.id.business_photos)
        businessReviewsRecycler = findViewById(R.id.business_reviews_and_ratings)
        seeAllReviews = findViewById(R.id.see_all_reviews)
        seeAllReviews.visibility = View.GONE

    }
}