package com.example.foodies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.Adapters.CategoryAdapter
import com.example.foodies.Adapters.PopularRestaurantsAdapter
import com.example.foodies.Models.Businesse
import com.example.foodies.ViewModels.CategoryBusinessViewModel

class CategoryBusinesses : AppCompatActivity() {

    private lateinit var categoryPoster:ImageView
    private lateinit var categoryName:TextView
    private lateinit var categoryDataRecycler:RecyclerView
    private lateinit var categoryRestaurantsAdapter:PopularRestaurantsAdapter
    private lateinit var viewModel:CategoryBusinessViewModel
    private var categoryData=ArrayList<Businesse>(1)
    private var userId:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_businesses)
        initialise()
        viewModel=ViewModelProvider(this).get(CategoryBusinessViewModel::class.java)
        Glide.with(this).load(intent.getStringExtra(CategoryAdapter.CATEGORY_IMAGE_BRIDGE)).into(categoryPoster)
        val name=intent.getStringExtra(CategoryAdapter.CATEGORY_NAME_BRIDGE)
        userId= intent.getStringExtra(MainActivity.USER_ID_BRIDGE)
        categoryName.text=name
        categoryDataRecycler.layoutManager=LinearLayoutManager(this)
        categoryRestaurantsAdapter= PopularRestaurantsAdapter(categoryData,this,userId!!,true)
        categoryDataRecycler.adapter=categoryRestaurantsAdapter
        viewModel.getCategoryData(name)?.observe(this, Observer {
            if(it!=null) {
                categoryData.clear()
                categoryData.addAll(it)
                categoryRestaurantsAdapter.notifyDataSetChanged()
            }
        })

    }

    private fun initialise() {
        categoryName=findViewById(R.id.category_name)
        categoryPoster=findViewById(R.id.category_poster)
        categoryDataRecycler=findViewById(R.id.category_data)
    }
}