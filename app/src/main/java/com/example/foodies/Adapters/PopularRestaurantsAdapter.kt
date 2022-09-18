package com.example.foodies.Adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.BusinessActivity
import com.example.foodies.MainActivity
import com.example.foodies.Models.Businesse
import com.example.foodies.R

class PopularRestaurantsAdapter(
    val popularRestaurants: ArrayList<Businesse>,
    val context: Context?,
    val userId: String
) : RecyclerView.Adapter<PopularRestaurantsAdapter.BusinessHolder>() {

    companion object{
        val BUSINESS_BRIDGE="business"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.restaurent_thumbnail_layout,parent,false)
        return BusinessHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessHolder, position: Int) {
        Log.d("TAG", "onBindViewHolder: ")
        Glide.with(holder.restaurantPoster.context).load(popularRestaurants.get(position).image_url).into(holder.restaurantPoster)
        holder.restaurantsName.text=popularRestaurants.get(position).name
        holder.restaurantsAddress.text=popularRestaurants.get(position).location?.address1
        holder.view.setOnClickListener {
            val intent=Intent(holder.view.context,BusinessActivity::class.java)
            intent.putExtra(BUSINESS_BRIDGE,popularRestaurants.get(position))
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return popularRestaurants.size
    }
    inner class BusinessHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantPoster:ImageView=itemView.findViewById(R.id.restaurant_poster)
        val restaurantsName:TextView=itemView.findViewById(R.id.restaurent_name)
        val restaurantsAddress:TextView=itemView.findViewById(R.id.business_address)
        val view=itemView
    }
}