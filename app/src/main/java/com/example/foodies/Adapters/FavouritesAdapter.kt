package com.example.foodies.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Models.Businesse
import com.example.foodies.R

class FavouritesAdapter(val businesses: ArrayList<BusinessDetailModel>,
                        val context: Context?,
                        val userId: String,
                        val isFullPoster: Boolean
) : RecyclerView.Adapter<FavouritesAdapter.BusinessHolder>()  {
    companion object{
        val IS_FAVOURITE_ADAPTER="adapter"
        val BUSINESS_BRIDGE="business"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.restaurent_thumbnail_layout,parent,false)
        return BusinessHolder(view)
    }

    override fun onBindViewHolder(holder: BusinessHolder, position: Int) {
        val business=businesses.get(position)
        Log.d("TAG", "onBindViewHolder: ${business.image_url}")
        if(isFullPoster){
            holder.view.layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT
        }

        holder.restaurantsName.text=business.name
        val location=business.location
        holder.restaurantsAddress.text=location?.address1+", "+location?.city + ", "+location?.state
        Glide.with(holder.view.context).asBitmap().load(business?.image_url).into(holder.restaurantPoster)
        Log.d("TAG", "onBindViewHolder: isClosed ${business.is_closed}")
        if(business.is_closed==false) {
            holder.isRestaurantOpen.text="closed"
            holder.isRestaurantOpen.setTextColor(Color.RED)
        }
        holder.category.text=business.categories?.get(0)?.title

        holder.restaurantRating.text=business.rating.toString()
        holder.view.setOnClickListener {
            val intent= Intent(holder.view.context, BusinessActivity::class.java)
            intent.putExtra(BUSINESS_BRIDGE,business)
            intent.putExtra(IS_FAVOURITE_ADAPTER,true)
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return businesses.size
    }

    inner class BusinessHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val restaurantPoster: ImageView =itemView.findViewById(R.id.restaurant_poster)
        val restaurantsName: TextView =itemView.findViewById(R.id.restaurent_name)
        val restaurantsAddress: TextView =itemView.findViewById(R.id.business_address)
        val isRestaurantOpen: TextView =itemView.findViewById(R.id.is_open)
        val restaurantRating: TextView =itemView.findViewById(R.id.business_rating)
        val category: TextView =itemView.findViewById(R.id.category)
        val view=itemView
    }
}