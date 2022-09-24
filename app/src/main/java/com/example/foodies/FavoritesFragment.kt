package com.example.foodies

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.FavouritesAdapter
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.ViewModels.FavoritesFragmentViewModel


class FavoritesFragment : Fragment() {

    private lateinit var favouritesRecycler:RecyclerView
    private var favouriteIds=ArrayList<String>(1)
    private var favouriteBusinesses=ArrayList<BusinessDetailModel>(1)
    private lateinit var favouritesAdapter:FavouritesAdapter
    private lateinit var viewModel: FavoritesFragmentViewModel
    private lateinit var userId:String

    companion object{
        var userFavouriteBusinesses=ArrayList<String>(1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true
        userId= arguments?.getString(MainActivity.USER_ID_BRIDGE).toString()
        arguments?.getStringArrayList(HomeActivity.FAVOURITES_BRIDGE)?.let { favouriteIds.addAll(it) }
        viewModel=ViewModelProvider(this).get(FavoritesFragmentViewModel::class.java)
        favouritesAdapter= FavouritesAdapter(favouriteBusinesses,this.requireContext(),userId,true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val intent=Intent()
        intent.setAction(HomeActivity.BOTTOM_NAV_UPDATER)
        intent.putExtra(HomeActivity.BOTTOM_NAV_UPDATER,1)
        LocalBroadcastManager.getInstance(this.requireContext()).sendBroadcast(intent)
        val view=inflater.inflate(R.layout.fragment_favorites, container, false)
        initialise(view)

        viewModel.getUserFavouriteBusineses(userId)?.observe(this.viewLifecycleOwner, Observer {
            if(it!=null) {
                userFavouriteBusinesses.clear()
                userFavouriteBusinesses.addAll(it)
                HomeActivity.userFavouriteBusinesses.clear()
                HomeActivity.userFavouriteBusinesses.addAll(it)
                viewModel.getFavourites(userId, userFavouriteBusinesses)?.observe(this.viewLifecycleOwner, Observer {
                    if(it!=null){
                        favouriteBusinesses.clear()
                        favouriteBusinesses.addAll(it)
                        favouritesAdapter.notifyDataSetChanged()
                    }
                })
            }
        })

        favouritesRecycler.layoutManager=LinearLayoutManager(this.requireContext())
        favouritesRecycler.adapter=favouritesAdapter

        return view
    }

    private fun initialise(view: View?) {
        favouritesRecycler=view?.findViewById(R.id.favourites)!!
    }

}