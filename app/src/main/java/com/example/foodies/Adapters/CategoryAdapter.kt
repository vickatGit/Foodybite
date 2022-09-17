package com.example.foodies.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.Models.CategoryThumbModel
import com.example.foodies.R

class CategoryAdapter(val categories: ArrayList<CategoryThumbModel>) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.category_thumb_layout,parent,false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        Glide.with(holder.categoryPoster.context).load(categories.get(position).categoryImage).into(holder.categoryPoster)
        holder.categoryName.text=categories.get(position).categoryName
    }

    override fun getItemCount(): Int {
        return categories.size
    }
    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryPoster:ImageView=itemView.findViewById(R.id.category_poster)
        val categoryName:TextView=itemView.findViewById(R.id.category_name)
    }
}