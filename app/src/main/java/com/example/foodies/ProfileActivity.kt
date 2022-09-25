package com.example.foodies

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.Adapters.BusinessReviewsAdapter
import com.example.foodies.Adapters.FavouritesAdapter
import com.example.foodies.Adapters.FriendsAdapter
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.ViewModels.ProfileActivityViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var userReviewsRecycler:RecyclerView
    private lateinit var userNoOfReviews:TextView
    private lateinit var userFollowers:TextView
    private lateinit var userFollowing:TextView
    private lateinit var userProfile:ImageView
    private lateinit var editProfile:Button
    private lateinit var follow:ToggleButton
    private lateinit var userName:TextView
    private lateinit var userEmail:TextView
    private lateinit var profileTitle:TextView
    private lateinit var followersContainer:LinearLayoutCompat
    private lateinit var followingContainer:LinearLayoutCompat
    private lateinit var viewModel:ProfileActivityViewModel
    private lateinit var userId:String
    private lateinit var profileId:String
    private var otherUser: Review?=null
    private var friendId: String?=null
    private var userFollowerIds=ArrayList<String>(1)
    private var userFollowingIds=ArrayList<String>(1)
    private lateinit var userReviewdBusinessesAdapter:FavouritesAdapter
    private var userReviewedBusinesses=ArrayList<BusinessDetailModel>(1)

    private var STORAGE_PERMISSION_CODE=123

    companion object{
        val FRIENDS_BRIDGE="friends_bridge"
        val ACTIVITY_TYPE="activity_title_text"
        var IS_USER_FOLLOWING_BRIDGE="user_following_ids"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        userId=intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        otherUser= intent.getParcelableExtra<Review>(BusinessReviewsAdapter.USER_REVIEW_BRIDGE)
        viewModel=ViewModelProvider(this).get(ProfileActivityViewModel::class.java)
        initialise()
        if(otherUser!=null){
            if(otherUser?.user?.id==userId) { MyProfile(userId) }
            else {
                otherUser?.user?.id?.let { OthersProfile(it) }
                viewModel.isUserFollowing(userId,otherUser?.user?.id)?.observe(this, Observer {
                    if(it) follow.isChecked=true else follow.isChecked=false
                })
            }

        }else{
            MyProfile(userId)
        }

        userReviewdBusinessesAdapter= FavouritesAdapter(userReviewedBusinesses,this,profileId,true)
        userReviewsRecycler.layoutManager=LinearLayoutManager(this)
        userReviewsRecycler.adapter=userReviewdBusinessesAdapter



        viewModel.getUser(profileId)?.observe(this, Observer {
            if(it!=null){
                userEmail.text=it.email
                userName.text=it.username
            }
        })
        viewModel.getFollowerIds(profileId)?.observe(this, Observer {
            if(it!=null){
                userFollowerIds.clear()
                userFollowerIds.addAll(it)
                userFollowers.text=it.size.toString()
            }
        })
        viewModel.getUserFollowing(profileId)?.observe(this, Observer {
            if(it!=null){
                userFollowingIds.clear()
                userFollowingIds.addAll(it)
                Log.d("TAG", "onCreate: userFollowingIds"+it)
                userFollowing.text = it.size.toString()
            }
        })
        viewModel.getUserReviewedBusinesses(profileId)?.observe(this, Observer {
            if(it!=null) {
                userReviewedBusinesses.clear()
                userReviewedBusinesses.addAll(it)
                userNoOfReviews.text = it.size.toString()
                userReviewdBusinessesAdapter.notifyDataSetChanged()
                follow.setOnClickListener {
                    if (follow.isChecked) {
                        viewModel.followUser(userId, otherUser?.user?.id)
                    } else {
                        viewModel.unfollowUser(userId, otherUser?.user?.id)
                    }

                }
            }
        })
        viewModel.getUserProfilePic(profileId)?.observe(this, Observer {
            if(it!=null)
                Glide.with(this).load(it).into(userProfile)
            else
                Glide.with(this).load(R.drawable.default_user_avatar).into(userProfile)
        })

        followingContainer.setOnClickListener {
            val intent=Intent(this,FriendsActivity::class.java)
            intent.putExtra(FRIENDS_BRIDGE,userFollowingIds)
            intent.putExtra(IS_USER_FOLLOWING_BRIDGE,userFollowingIds)
            intent.putExtra(ACTIVITY_TYPE,"Following")
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            startActivity(intent)
        }
        followersContainer.setOnClickListener {
            val intent=Intent(this,FriendsActivity::class.java)
            intent.putExtra(FRIENDS_BRIDGE,userFollowerIds)
            intent.putExtra(IS_USER_FOLLOWING_BRIDGE,userFollowingIds)
            intent.putExtra(ACTIVITY_TYPE,"Follower")
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            startActivity(intent)
        }

        userProfile.setOnClickListener {
            CheckIsPermissionIsGranted()
        }
    }

    private fun CheckIsPermissionIsGranted(){
        Log.d("TAG", "CheckIsPermissionIsGranted: ${checkCallingOrSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED}")
        if(checkCallingOrSelfPermission(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listOf(android.Manifest.permission.MANAGE_EXTERNAL_STORAGE).toTypedArray(),STORAGE_PERMISSION_CODE)
            }
        }else{
            val intent= Intent()
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent,"Select Image"),STORAGE_PERMISSION_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==STORAGE_PERMISSION_CODE){
            if(grantResults.size>0 && grantResults.get(0)==PackageManager.PERMISSION_GRANTED){
                CheckIsPermissionIsGranted()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==STORAGE_PERMISSION_CODE){
            val data=data?.data
            val bitmap=BitmapFactory.decodeStream(data?.let { contentResolver.openInputStream(it) })
            userProfile.setImageBitmap(bitmap)
            viewModel.storeUserProfilePic(profileId,bitmap,data)
        }
    }
    private fun MyProfile(id: String) {
        follow.visibility=View.INVISIBLE
        userProfile.isEnabled=true
        profileId=id
        profileTitle.text="My Profile"
    }
    private fun OthersProfile(id: String) {
        follow.visibility=View.VISIBLE
        userProfile.isEnabled=false
        profileId=id
        profileTitle.text="Profile"

    }
    private fun initialise() {
        userReviewsRecycler=findViewById(R.id.user_reviewed_businesses)
        userNoOfReviews=findViewById(R.id.user_reviews)
        userFollowers=findViewById(R.id.user_followers)
        userFollowing=findViewById(R.id.user_following)
        userProfile=findViewById(R.id.user_profile)
        userName=findViewById(R.id.name)
        profileTitle=findViewById(R.id.profile_text)
        followersContainer=findViewById(R.id.followers)
        followingContainer=findViewById(R.id.following)
        userEmail=findViewById(R.id.user_email)
        follow=findViewById(R.id.follow)
    }
}