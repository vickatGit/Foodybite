package com.example.foodies

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.CategoryAdapter
import com.example.foodies.Adapters.PopularRestaurantsAdapter
import com.example.foodies.Adapters.SearchedDataAdapter
import com.example.foodies.Models.Businesse
import com.example.foodies.Respository.DataRepository
import com.example.foodies.Static.CategoryDataSource
import com.example.foodies.ViewModels.HomeFragmentViewModel
import com.google.gson.Gson


class HomeFragment : Fragment() {

    private lateinit var popularRestaurantsRecycler:RecyclerView
    private  var popularRestaurants=ArrayList<Businesse>(1)
    private  var searchedData=ArrayList<Businesse>(1)
    private lateinit var popularRestaurantsAdapter:PopularRestaurantsAdapter
    private lateinit var viewModel:HomeFragmentViewModel
    private lateinit var searchCard:CardView
    private lateinit var searcheProgress:ProgressBar
    private lateinit var searcher:androidx.appcompat.widget.SearchView
    private lateinit var categoriesRecycler:RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var userId:String
    private lateinit var searchedDataRecycler:RecyclerView
    private lateinit var searchedDataAdapter: SearchedDataAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        viewModel=ViewModelProvider(this).get(HomeFragmentViewModel::class.java)

    }

    override fun onResume() {
        super.onResume()
        val intent= Intent()
        intent.setAction(HomeActivity.BOTTOM_NAV_UPDATER)
        intent.putExtra(HomeActivity.BOTTOM_NAV_UPDATER,0)
        LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view=inflater.inflate(R.layout.fragment_home, container, false)
        userId=arguments?.getString(MainActivity.USER_ID_BRIDGE).toString()
        initialise(view)
        popularRestaurantsRecycler.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)
        searchedDataRecycler.layoutManager=LinearLayoutManager(this.context)
        categoriesRecycler.layoutManager=LinearLayoutManager(this.context,LinearLayoutManager.HORIZONTAL,false)

        popularRestaurantsAdapter=PopularRestaurantsAdapter(popularRestaurants, this.context, userId, false)
        popularRestaurantsRecycler.adapter=popularRestaurantsAdapter
        categoryAdapter= CategoryAdapter(CategoryDataSource.categoriesInitialiser(),userId,this.requireContext())
        categoriesRecycler.adapter=categoryAdapter
        searchedDataAdapter=SearchedDataAdapter(searchedData, this.context, userId, true)
        searchedDataRecycler.adapter=searchedDataAdapter



        viewModel.getPopularRestaurants()?.observe(this.viewLifecycleOwner, Observer {
            if(it!=null){
                popularRestaurants.clear()
                popularRestaurants.addAll(it)
                Log.d(DataRepository.TAG, "getFamousRestaurants: $it")
                popularRestaurantsAdapter.notifyDataSetChanged()
            }
        })
        viewModel.search("")?.observe(this.viewLifecycleOwner, Observer {
            if(it!=null) {
                searcheProgress.visibility=View.GONE
                Log.d("TAG", "onCreateView: search "+Gson().toJson(it))
                searchedData.clear()
                searchedData.addAll(it)
                searchedDataAdapter.notifyDataSetChanged()
            }
        })
        searcher.setOnQueryTextListener(object: androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.isEmpty() == false) {
                    viewModel.search(query)
                    searcheProgress.visibility=View.VISIBLE
                    searchedDataRecycler.visibility = View.VISIBLE
                }
                else {
                    searchedDataRecycler.visibility = View.GONE
                }
                return true

            }

            override fun onQueryTextChange(newText: String?): Boolean {

                searchedDataAdapter.notifyDataSetChanged()
                if(newText?.isEmpty() == true) {
                    searchedDataRecycler.visibility = View.GONE
                    searchedData.clear()
                }else{
                }
                return true
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
        searcheProgress=view.findViewById(R.id.progress)
        searchedDataRecycler=view.findViewById(R.id.searched_data)
        categoriesRecycler=view.findViewById(R.id.categories)
        val textView:TextView = searcher.findViewById(androidx.appcompat.R.id.search_src_text);
        textView.setTextColor(Color.GRAY);

    }
}