package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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

    companion object{
        var userFavouriteBusinesses=ArrayList<String>(1)
        val context=this
        val FAVOURITES_BRIDGE="favourites_passer"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        userId= intent.getStringExtra(MainActivity.USER_ID_BRIDGE).toString()
        initialise()
        viewModel=ViewModelProvider(this).get(HomeActivityViewModel::class.java)
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
            }
            return@setOnItemSelectedListener true
        }
    }
    fun initialise(){
        bottomNavigationView=findViewById(R.id.bottom_navigation_view)
    }
}