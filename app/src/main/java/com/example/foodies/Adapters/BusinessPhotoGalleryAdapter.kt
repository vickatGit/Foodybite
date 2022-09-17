package com.example.foodies.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.R

class BusinessPhotoGalleryAdapter(val businessImages: ArrayList<String>) : RecyclerView.Adapter<BusinessPhotoGalleryAdapter.BusinessImageHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessImageHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.business_gallery_image_layout,parent,false)
        return BusinessImageHolder(view)

    }

    override fun onBindViewHolder(holder: BusinessImageHolder, position: Int) {
        Glide.with(holder.BusinessImage).load(businessImages.get(position)).into(holder.BusinessImage)
    }

    override fun getItemCount(): Int {
        return businessImages.size
    }
    inner class BusinessImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val BusinessImage:ImageView=itemView.findViewById(R.id.business_image)
    }

}