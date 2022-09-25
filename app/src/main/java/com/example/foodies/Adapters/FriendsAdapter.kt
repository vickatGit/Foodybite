package com.example.foodies.Adapters

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.MainActivity
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessReviewModel.User
import com.example.foodies.Models.UserModel
import com.example.foodies.ProfileActivity
import com.example.foodies.R
import com.example.foodies.ViewModels.FriendsActivityViewModel

class FriendsAdapter(
    val allFriends: ArrayList<UserModel>,
    val userFollowingIds: ArrayList<String>,
    val viewModel: FriendsActivityViewModel,
    val userId: String
) : RecyclerView.Adapter<FriendsAdapter.FriendHolder>() {

    companion object{
        val FRIEND_ID="friend_id_passer"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.friend_item,parent,false)
        return FriendHolder(view)
    }

    override fun onBindViewHolder(holder: FriendHolder, position: Int) {
        val user=allFriends.get(position)
        Glide.with(holder.friendPic).load(user.userImage).into(holder.friendPic)
        holder.friendName.text=user.username
        holder.friendNoOfReviews.visibility=View.INVISIBLE
            Log.d("TAG", "onBindViewHolder: isFollowing"+user.userRef)
        if(userFollowingIds.contains(user.userRef)) {
            holder.followStatus.isChecked = true
        }
        holder.followStatus.setOnClickListener {
            if(holder.followStatus.isChecked) {
                viewModel.followUser(userId,user.userRef)
            } else {
                viewModel.unfollowUser(userId, user.userRef)
            }

        }
        holder.view.setOnClickListener {
            val intent= Intent(holder.friendPic.context, ProfileActivity::class.java)
            val review=Review("",5,"","",null,User(user.userRef.toString(),user.userImage,user.username,null))
            intent.putExtra(BusinessReviewsAdapter.USER_REVIEW_BRIDGE,review)
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allFriends.size
    }
    inner class FriendHolder(view: View) : RecyclerView.ViewHolder(view){
        val friendPic:ImageView=view.findViewById(R.id.user_pic)
        val friendName:TextView=view.findViewById(R.id.friend_name)
        val friendNoOfReviews:TextView=view.findViewById(R.id.friend_no_of_reviews)
        val followStatus:ToggleButton=view.findViewById(R.id.follow)
        val view:View=view
    }

}