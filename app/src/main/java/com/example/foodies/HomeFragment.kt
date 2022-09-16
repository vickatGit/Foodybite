package com.example.foodies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.PopularRestaurantsAdapter
import com.example.foodies.Models.Businesse
import com.example.foodies.ViewModels.HomeFragmentViewModel


class HomeFragment : Fragment() {

    private lateinit var popularRestaurantsRecycler:RecyclerView
    private  var popularRestaurants=ArrayList<Businesse>(1)
    private lateinit var popularRestaurantsAdapter:PopularRestaurantsAdapter
    private lateinit var viewModel:HomeFragmentViewModel

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
        popularRestaurantsAdapter=PopularRestaurantsAdapter(popularRestaurants)
        popularRestaurantsRecycler.adapter=popularRestaurantsAdapter
        viewModel.getPopularRestaurants()?.observe(this.viewLifecycleOwner, Observer {
            if(it!=null){
                popularRestaurants.clear()
                popularRestaurants.addAll(it)
                popularRestaurantsAdapter.notifyDataSetChanged()
            }
        })

        return view
    }

    private fun initialise(view: View) {
        popularRestaurantsRecycler=view.findViewById(R.id.popular_restaurants)
    }
}