package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

    companion object{

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initialise()
        viewModel=ViewModelProvider(this).get(HomeActivityViewModel::class.java)

        supportFragmentManager.beginTransaction().replace(R.id.frag_placeholder,HomeFragment(),"home_fragment").addToBackStack(null).commit()

        bottomNavigationView.background=null
//        val retro=RetroHelper.getInstance()
//        val body=retro.create(BusinessFetcher::class.java).search("Bearer $API_KEY","pizza","San Francisko,CA").enqueue(object : Callback<Any> {
//            override fun onResponse(call: Call<Any>, response: Response<Any>) {
//                Log.d("TAG", "onCreate: body ${Gson().toJson(response.body())}")
//            }
//
//            override fun onFailure(call: Call<Any>, t: Throwable) {
//                Log.d("TAG", "onFailure: ${t.localizedMessage}")
//            }
//
//        })
    }
    fun initialise(){
        bottomNavigationView=findViewById(R.id.bottom_navigation_view)
    }
}