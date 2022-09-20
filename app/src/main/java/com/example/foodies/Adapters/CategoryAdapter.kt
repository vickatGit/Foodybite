package com.example.foodies.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodies.CategoryBusinesses
import com.example.foodies.MainActivity
import com.example.foodies.Models.CategoryThumbModel
import com.example.foodies.R

class CategoryAdapter(
    val categories: ArrayList<CategoryThumbModel>,
    val userId: String,
    val context: Context
) : RecyclerView.Adapter<CategoryAdapter.CategoryHolder>() {

    companion object{
        val CATEGORY_NAME_BRIDGE="category_name_passer"
        val CATEGORY_IMAGE_BRIDGE="category_image_passer"
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.category_thumb_layout,parent,false)
        return CategoryHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        Glide.with(holder.categoryPoster.context).load(categories.get(position).categoryImage).into(holder.categoryPoster)
        holder.categoryName.text=categories.get(position).categoryName
        holder.view.setOnClickListener {
            val intent=Intent(holder.view.context,CategoryBusinesses::class.java)
            intent.putExtra(CATEGORY_NAME_BRIDGE,categories.get(position).categoryName)
            intent.putExtra(CATEGORY_IMAGE_BRIDGE,categories.get(position).categoryImage)
            intent.putExtra(MainActivity.USER_ID_BRIDGE,userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }
    inner class CategoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val categoryPoster:ImageView=itemView.findViewById(R.id.category_poster)
        val categoryName:TextView=itemView.findViewById(R.id.category_name)
        val view=itemView
    }
}