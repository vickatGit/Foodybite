package com.example.foodies

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessReviewModel.User
import com.example.foodies.Models.UserModel
import com.example.foodies.ViewModels.RatingReviewActivityViewModel
import com.google.android.material.card.MaterialCardView
import java.util.*
import kotlin.collections.ArrayList

class RatingReviewActivity : AppCompatActivity() {

    private lateinit var star1:ImageView
    private lateinit var star2:ImageView
    private lateinit var star3:ImageView
    private lateinit var star4:ImageView
    private lateinit var star5:ImageView
    private lateinit var submitRatingAndReview:Button
    private lateinit var progress:ProgressBar
    private lateinit var review:EditText
    private lateinit var ratingcard:MaterialCardView
    private var rating:Int?=null
    private var stars=ArrayList<ImageView>(5)
    private lateinit var viewModel:RatingReviewActivityViewModel
    private lateinit var userId:String
    private lateinit var userInfo:UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating_review)
        val businessId=intent.getStringExtra(BusinessActivity.BUSINESS_ID_BRIDGE)
        userId=intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        viewModel=ViewModelProvider(this).get(RatingReviewActivityViewModel::class.java)
        viewModel.getUserInfo(userId)?.observe(this, Observer {
            userInfo=it!!
            Log.d("TAG", "onCreate: userInfo image $userInfo")
        })
        initialise()
        star1.setOnClickListener {
            fillStars(0)
            rating=1
        }
        star2.setOnClickListener {
            fillStars(1)
            rating=2
        }
        star3.setOnClickListener {
            fillStars(2)
            rating=3
        }
        star4.setOnClickListener {
            fillStars(3)
            rating=4
        }
        star5.setOnClickListener {
            fillStars(4)
            rating=5
        }
        submitRatingAndReview.setOnClickListener {
            if(rating!=null && !review.text.isEmpty()){
                progress.visibility= View.VISIBLE
                ratingcard.visibility=View.INVISIBLE
                review.visibility=View.INVISIBLE

                val userReview=Review(businessId!!, rating!!.toInt(),review.text.toString(),Date().toString(),userId, User(userId,userInfo.userImage,userInfo.username,userInfo.userImage))
                viewModel.saveReview(userReview,userId)?.observe(this, Observer {
                    if(it==true){
                        Toast.makeText(this,"Review Posted",Toast.LENGTH_SHORT)
                        progress.visibility= View.INVISIBLE
                        ratingcard.visibility=View.VISIBLE
                        review.visibility=View.VISIBLE
                        finish()
                    }else{
                        progress.visibility= View.INVISIBLE
                        ratingcard.visibility=View.VISIBLE
                        review.visibility=View.VISIBLE
                        Toast.makeText(this,"Failed to post Review",Toast.LENGTH_SHORT)
                    }
                })
            }
        }
    }

    private fun fillStars(rating: Int) {
        for (i in 0..4){
            if(i<=rating)
                stars.get(i).setColorFilter(Color.rgb(255,204,0))
            else
                stars.get(i).setColorFilter(Color.rgb(223,228,237))
        }
    }

    private fun initialise() {
        submitRatingAndReview=findViewById(R.id.submit_rating_and_review)
        review=findViewById(R.id.review)
        progress=findViewById(R.id.progress)
        ratingcard=findViewById(R.id.rating_card)
        star1=findViewById(R.id.star_1)
        star2=findViewById(R.id.star_2)
        star3=findViewById(R.id.star_3)
        star4=findViewById(R.id.star_4)
        star5=findViewById(R.id.star_5)

        stars.add(star1)
        stars.add(star2)
        stars.add(star3)
        stars.add(star4)
        stars.add(star5)

    }
}