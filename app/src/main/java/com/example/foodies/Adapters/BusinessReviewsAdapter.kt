package com.example.foodies.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.MainActivity
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.ProfileActivity
import com.example.foodies.R
import com.example.foodies.ViewModels.BusinessActivityViewModel
import kotlin.collections.ArrayList

class BusinessReviewsAdapter(
    val reviews: ArrayList<Review>,
    val isWholeReview: Boolean,
    val userId: String,
    val userProfilePicGetter: BusinessActivityViewModel,
    val lifecycle: AppCompatActivity
) : RecyclerView.Adapter<BusinessReviewsAdapter.ReviewThumbnailHolder>() {
    companion object{
        val USER_REVIEW_BRIDGE="user_review_passer"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewThumbnailHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.review_thumbnail,parent,false)
        return ReviewThumbnailHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewThumbnailHolder, position: Int) {
        holder.userReviewThumb.setSingleLine(isWholeReview)
        val reviews=this.reviews.get(position)

        if(reviews.user.image_url!=null && !reviews.user.image_url.isEmpty())
            Glide.with(holder.userProfile).load(reviews.user.image_url).into(holder.userProfile)
        else
            Glide.with(holder.userProfile).load(R.drawable.default_user_avatar).into(holder.userProfile)
        Log.d("TAG", "onBindViewHolder: Review Images ${reviews.time_created} ")
        holder.userName.text=reviews?.user?.name
        holder.userRating.text=reviews?.rating.toString()
        holder.userReviewThumb.text=reviews?.text
        holder.view.setOnClickListener {
            val intent= Intent(holder.userReviewThumb.context,ProfileActivity::class.java)
            intent.putExtra(USER_REVIEW_BRIDGE,reviews)
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            holder.userReviewThumb.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return reviews.size
    }
    inner class ReviewThumbnailHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val userProfile:ImageView=itemView.findViewById(R.id.user_profile)
        val userName:TextView=itemView.findViewById(R.id.user_name)
        val userReviewThumb:TextView=itemView.findViewById(R.id.user_review_thumbnail)
        val userRating:TextView=itemView.findViewById(R.id.user_rating)
        val view:View=itemView
    }
}