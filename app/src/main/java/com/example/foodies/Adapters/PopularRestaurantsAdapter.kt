package com.example.foodies.Adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.Models.Businesse
import com.example.foodies.R

class PopularRestaurantsAdapter(val popularRestaurants: ArrayList<Businesse>) : RecyclerView.Adapter<PopularRestaurantsAdapter.BusinessHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.restaurent_thumbnail_layout,parent,false)
        return BusinessHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        Glide.with(holder.restaurantPoster.context).load(popularRestaurants.get(position).image_url).into(holder.restaurantPoster)
        holder.restaurantsName.text=popularRestaurants.get(position).name
        holder.restaurantsAddress.text=popularRestaurants.get(position).location.address1
        holder.view.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return popularRestaurants.size
    }
    inner class BusinessHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantPoster:ImageView=itemView.findViewById(R.id.restaurant_poster)
        val restaurantsName:TextView=itemView.findViewById(R.id.restaurent_name)
        val restaurantsAddress:TextView=itemView.findViewById(R.id.restaurent_address)
        val view=itemView
    }
}