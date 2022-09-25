package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.FriendsAdapter
import com.example.foodies.Models.UserModel
import com.example.foodies.ViewModels.FriendsActivityViewModel

class FriendsActivity : AppCompatActivity() {

    private lateinit var friendsListRecycler:RecyclerView
    private lateinit var activityTitle:TextView
    private lateinit var friendsListAdapter:FriendsAdapter
    private lateinit var userId:String
    private lateinit var title:String
    private lateinit var viewModel:FriendsActivityViewModel
    private var friendsListIds=ArrayList<String>(1)
    private var userFollowingIds=ArrayList<String>(1)
    private var allFriends=ArrayList<UserModel>(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friends)
        viewModel=ViewModelProvider(this).get(FriendsActivityViewModel::class.java)
        initialise()
        friendsListIds= intent.getStringArrayListExtra(ProfileActivity.FRIENDS_BRIDGE)!!
        userId= intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        title= intent.getStringExtra(ProfileActivity.ACTIVITY_TYPE).toString()
        userFollowingIds.addAll(intent.getStringArrayListExtra(ProfileActivity.IS_USER_FOLLOWING_BRIDGE)!!)

        friendsListAdapter= FriendsAdapter(allFriends,userFollowingIds,viewModel,userId)
        friendsListRecycler.layoutManager=LinearLayoutManager(this)
        friendsListRecycler.adapter=friendsListAdapter
        activityTitle.text=title
        viewModel.getFriends(friendsListIds)?.observe(this, Observer {
            allFriends.clear()
            allFriends.addAll(it)
            Log.d("TAG", "onCreate: getFriends $it")
            friendsListAdapter.notifyDataSetChanged()
        })



    }

    private fun initialise() {
        friendsListRecycler=findViewById(R.id.friend_list)
        activityTitle=findViewById(R.id.profile_text)
    }
}