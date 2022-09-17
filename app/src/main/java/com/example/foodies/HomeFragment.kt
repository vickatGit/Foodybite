package com.example.foodies

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.CategoryAdapter
import com.example.foodies.Adapters.PopularRestaurantsAdapter
import com.example.foodies.Models.Businesse
import com.example.foodies.Static.CategoryDataSource
import com.example.foodies.ViewModels.HomeFragmentViewModel


class HomeFragment : Fragment() {

    private lateinit var popularRestaurantsRecycler:RecyclerView
    private  var popularRestaurants=ArrayList<Businesse>(1)
    private lateinit var popularRestaurantsAdapter:PopularRestaurantsAdapter
    private lateinit var viewModel:HomeFragmentViewModel
    private lateinit var searchCard:CardView
    private lateinit var searcher:androidx.appcompat.widget.SearchView
    private lateinit var categoriesRecycler:RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel=ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("TAG", "onCreateView: homefragment")
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        initialise(view)
        popularRestaurantsRecycler.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        popularRestaurantsAdapter=PopularRestaurantsAdapter(popularRestaurants,this.context)
        popularRestaurantsRecycler.adapter=popularRestaurantsAdapter

        categoriesRecycler.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        categoryAdapter= CategoryAdapter(CategoryDataSource.categoriesInitialiser())
        categoriesRecycler.adapter=categoryAdapter
        viewModel.getPopularRestaurants()?.observe(this.viewLifecycleOwner, Observer {
            if(it!=null){
                popularRestaurants.clear()
                popularRestaurants.addAll(it)
                popularRestaurantsAdapter.notifyDataSetChanged()
            }
        })

        searchCard.setOnClickListener {
            searcher.isIconified=false
        }

        return view
    }

    private fun initialise(view: View) {
        popularRestaurantsRecycler=view.findViewById(R.id.popular_restaurants)
        searchCard=view.findViewById(R.id.search_card)
        searcher=view.findViewById(R.id.search)
        categoriesRecycler=view.findViewById(R.id.categories)
        val textView:TextView = searcher.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.GRAY);
    }
}