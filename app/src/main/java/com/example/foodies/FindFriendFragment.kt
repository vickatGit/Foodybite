package com.example.foodies

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodies.Adapters.FriendsAdapter
import com.example.foodies.Models.UserModel
import com.example.foodies.ViewModels.FindFriendFragmentViewModel
import com.example.foodies.ViewModels.FriendsActivityViewModel


class FindFriendFragment : Fragment() {


    private lateinit var searchView:androidx.appcompat.widget.SearchView
    private lateinit var searchedFriendsRecycler:RecyclerView
    private lateinit var viewModel:FriendsActivityViewModel
    private lateinit var searchedFriendsAdapter:FriendsAdapter
    private lateinit var searchCard:CardView
    private var searchedFriends=ArrayList<UserModel>(1)

    private var userFollowingIds=ArrayList<String>(1)
    private lateinit var userId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance=true

        userId=arguments?.getString(MainActivity.USER_ID_BRIDGE).toString()
        userFollowingIds.addAll(arguments?.getStringArrayList(ProfileActivity.IS_USER_FOLLOWING_BRIDGE)!!)
        viewModel=ViewModelProvider(this).get(FriendsActivityViewModel::class.java)
        searchedFriendsAdapter= FriendsAdapter(searchedFriends,userFollowingIds,viewModel,userId)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view=inflater.inflate(R.layout.fragment_find_friend, container, false)
        initialise(view)
        searchedFriendsRecycler.layoutManager=LinearLayoutManager(this.context)
        searchedFriendsRecycler.adapter=searchedFriendsAdapter
        viewModel.findUser("")?.observe(this.viewLifecycleOwner, Observer {
            searchedFriends.clear()
            searchedFriends.addAll(it)
            Log.d("TAG", "onCreateView: searchedFriends $it")
            searchedFriendsAdapter.notifyDataSetChanged()
        })
        searchView.setOnQueryTextListener(object :androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.findUser(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.isEmpty()==true) {
                    searchedFriends.clear()
                    searchedFriendsAdapter.notifyDataSetChanged()
                }
                return true
            }

        })
        searchCard.setOnClickListener {
            searchView.isIconified=false
        }
        return view
    }
    private fun initialise(view: View) {
        searchView=view.findViewById(R.id.search_friends)
        searchedFriendsRecycler=view.findViewById(R.id.searched_friends)
        searchCard=view.findViewById(R.id.search_card)

    }


}