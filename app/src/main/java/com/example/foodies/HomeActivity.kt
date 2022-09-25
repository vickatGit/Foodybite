package com.example.foodies

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.get
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.foodies.Networking.BusinessFetcher
import com.example.foodies.Networking.RetroHelper
import com.example.foodies.ViewModels.HomeActivityViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView:BottomNavigationView
    private lateinit var viewModel:HomeActivityViewModel
    private lateinit var userId:String

    private val BottomUpdatesReciever:BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("TAG", "onReceive: broadcast")
            if(intent!=null){
                val pos=intent.getIntExtra(BOTTOM_NAV_UPDATER,0)
                bottomNavigationView.menu.get(pos).setChecked(true)
            }
        }

    }




    companion object{
        val BOTTOM_NAV_UPDATER="updating_botttom_navigation"
        var userFavouriteBusinesses=ArrayList<String>(1)
        var userFollowingIds=ArrayList<String>(1)
        val context=this
        val FAVOURITES_BRIDGE="favourites_passer"
        val HOME="home"
        val FAVOURITES="favourites"
        val PROFILE="profile"
        enum class AppState{ HOME,FAVOURITES,PROFILE }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        LocalBroadcastManager.getInstance(this).registerReceiver(BottomUpdatesReciever, IntentFilter(BOTTOM_NAV_UPDATER) )
        userId= intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        initialise()
        viewModel=ViewModelProvider(this).get(HomeActivityViewModel::class.java)
        viewModel.getUserFollowing(userId)?.observe(this, Observer {
            Log.d("TAG", "onCreate: getUserFollowing $it")
            if(it!=null){
                userFollowingIds.clear()
                userFollowingIds.addAll(it)
            }
        })
        viewModel.getUserFavouriteBusineses(userId)?.observe(this, Observer {
            if(it!=null) {
                userFavouriteBusinesses.clear()
                userFavouriteBusinesses.addAll(it)
            }

        })

        val homeFragment=HomeFragment()
        val bundle=Bundle()
        bundle.putString(MainActivity.USER_ID_BRIDGE,userId)
        homeFragment.arguments=bundle
        supportFragmentManager.beginTransaction().replace(R.id.frag_placeholder,homeFragment,"home_fragment").addToBackStack(null).commit()

        bottomNavigationView.background=null
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.bookmarks -> {
                    val frag=FavoritesFragment()
                    val bundle=Bundle()
                    bundle.putStringArrayList(FAVOURITES_BRIDGE, FavoritesFragment.userFavouriteBusinesses)
                    bundle.putString(MainActivity.USER_ID_BRIDGE,userId)
                    frag.arguments=bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frag_placeholder,frag,"favourites_fragment").addToBackStack(null).commit()
                }
                R.id.home ->{
                    val homeFragment=HomeFragment()
                    val bundle=Bundle()
                    bundle.putString(MainActivity.USER_ID_BRIDGE,userId)
                    homeFragment.arguments=bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frag_placeholder,homeFragment,"home_fragment").addToBackStack(null).commit()
                }
                R.id.user -> {
                    val intent=Intent(this,ProfileActivity::class.java)
                    intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
                    startActivity(intent)
                }
                R.id.search_friend -> {
                    val frag=FindFriendFragment()
                    val bundle=Bundle()
                    bundle.putString(MainActivity.USER_ID_BRIDGE,userId)
                    bundle.putStringArrayList(ProfileActivity.IS_USER_FOLLOWING_BRIDGE, userFollowingIds)
                    bundle.putStringArrayList("users", userFollowingIds)
                    frag.arguments=bundle
                    supportFragmentManager.beginTransaction().replace(R.id.frag_placeholder,frag,"find_friend_fragment").addToBackStack(null).commit()
                }
            }
            return@setOnItemSelectedListener true
        }
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentByTag("home_fragment")!=null)
            finish()
        super.onBackPressed()
    }
    fun initialise(){
        bottomNavigationView=findViewById(R.id.bottom_navigation_view)
    }
}