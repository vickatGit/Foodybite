package com.example.foodies.Respository

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.foodies.Models.BusinessModel
import com.example.foodies.Models.BusinessReviewModel.BusinessReviewsModel
import com.example.foodies.Models.BusinessReviewModel.Review
import com.example.foodies.Models.BusinessReviewModel.User
import com.example.foodies.Models.BusinessViewerModel.BusinessDetailModel
import com.example.foodies.Models.Businesse
import com.example.foodies.Models.FollowingModels.FollowingModel
import com.example.foodies.Models.UserModel
import com.example.foodies.Networking.BusinessFetcher
import com.example.foodies.Networking.RetroHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class DataRepository {
    private val API_KEY="cfo72-p8TKk8JVCClHts2-F2aLDHmaitKVgmwW47YsXUZMpuos_hWoM_0iESLi7mmelKnQeVZLLoEp3eDK7pPO-v5m1AMxIra57QJ6LgdYRfbvejxnx6gWlWXXccY3Yx"

    private val db=FirebaseFirestore.getInstance()
    private val dbStorage=FirebaseStorage.getInstance()
    private val isEmailAlreadyExists:MutableLiveData<Boolean> = MutableLiveData()
    val isUserExist:MutableLiveData<String> = MutableLiveData()
    private val isUserCreated:MutableLiveData<String> = MutableLiveData()
    private var userProfilePic:MutableLiveData<Uri?> = MutableLiveData()
    private var User:MutableLiveData<UserModel> = MutableLiveData()
    private var AllFriends:MutableLiveData<List<UserModel>> = MutableLiveData()

    val popularRestaurants:MutableLiveData<List<Businesse>> = MutableLiveData()
    val searchedBusinesses:MutableLiveData<List<Businesse>> = MutableLiveData()
    val businessDetails:MutableLiveData<BusinessDetailModel> = MutableLiveData()
    val businessReviews:MutableLiveData<BusinessReviewsModel> = MutableLiveData()
    val dbReviews:MutableLiveData<List<Review>> = MutableLiveData()
    val isReviewSaved:MutableLiveData<Boolean> = MutableLiveData()
    val userFavouriteBusinessesIds:MutableLiveData<List<String>> = MutableLiveData()
    val userInfo:MutableLiveData<UserModel> = MutableLiveData()
    val IsUserFollowing:MutableLiveData<Boolean> = MutableLiveData()
    val followerIds:MutableLiveData<List<String>> = MutableLiveData()
    val followingIds:MutableLiveData<List<String>> = MutableLiveData()
    val userFavouritesBusinesses:MutableLiveData<List<BusinessDetailModel>> = MutableLiveData()
    companion object{
        val TAG="tag"
        private var dataRepo:DataRepository= DataRepository()
        fun getInstance(): DataRepository { return dataRepo }
    }
    private constructor()
    fun registerUser(user: UserModel): MutableLiveData<String> {
        val docId=db.collection("Users").document().id

        CoroutineScope(Dispatchers.IO).launch {
            val hashedPassword=BCrypt.withDefaults().hashToString(6,user.password?.toCharArray())
            user.password=hashedPassword
            user.userRef=docId
            db.collection("Users").document(docId).set(user).addOnCompleteListener {
                if(it.isSuccessful){
                    isUserCreated.postValue(docId)
                }
            }
        }
        return isUserCreated

    }

    fun getUser(userId:String): MutableLiveData<UserModel> {
        db.collection("Users").document(userId).get().addOnCompleteListener {
            if(it.isSuccessful){
                val user=UserModel(it.result.get("username").toString(),it.result.get("email").toString(),null,null,null)
                User.postValue(user)
            }
        }
        return User
    }
    fun isEmailAlreayExists(email: String): MutableLiveData<Boolean> {
        db.collection("Users").whereEqualTo("email",email).get().addOnCompleteListener {
            if(it.isSuccessful) {
                Log.d(TAG, "isEmailAlreayExists: ${it.result.documents.size}")
                if (it.result.documents.size > 0) isEmailAlreadyExists.postValue(true) else isEmailAlreadyExists.postValue(false)
            }
            else
                isEmailAlreadyExists.postValue(false)
        }
        return isEmailAlreadyExists
    }
    fun isUserExist(email:String,password:String): MutableLiveData<String> {
        db.collection("Users").whereEqualTo("email",email).get().addOnCompleteListener {
            if(it.isSuccessful){
                val hashedPassword=it.result.documents.get(0).get("password").toString()
                if(BCrypt.verifyer().verify(password.toCharArray(),hashedPassword).verified)
                    isUserExist.postValue(it.result.documents.get(0).getString("userRef"))
                else
                    isUserExist.postValue(null)
            }else{
                isUserExist.postValue(null)
            }
        }
        return isUserExist
    }

    fun getFamousRestaurants(): MutableLiveData<List<Businesse>> {
        val retro=RetroHelper.getInstance()
        val famousRestaurants=ArrayList<Businesse>(1)
        retro.create(BusinessFetcher::class.java)
            .getPopularRestaurants("Bearer $API_KEY","San Francisco, CA","indpak,restaurants","rating", arrayOf("hot_and_new"))
            .enqueue(object :Callback<BusinessModel>{
                override fun onResponse(
                    call: Call<BusinessModel>,
                    response: Response<BusinessModel>
                ) {
                    Log.d(TAG, "onResponse: $response")
                    if(response.isSuccessful){
                        if(response.body()!=null){
                            Log.d(TAG, "onResponse: "+Gson().toJson(response.body()))
                            famousRestaurants.addAll(response?.body()!!.businesses)
                            popularRestaurants.postValue(famousRestaurants)
                        }
                    }
                }

                override fun onFailure(call: Call<BusinessModel>, t: Throwable) {
                    Log.d(TAG, "onFailure: ${t.localizedMessage}")
                }

            })
        return popularRestaurants
    }

    fun getBusiness(id: String?): MutableLiveData<BusinessDetailModel> {
        val retro=RetroHelper.getInstance().create(BusinessFetcher::class.java)
        retro.getBusiness("Bearer $API_KEY",id!!).enqueue(object:Callback<BusinessDetailModel>{
            override fun onResponse( call: Call<BusinessDetailModel>, response: Response<BusinessDetailModel>) {
                businessDetails.postValue(response.body())
                Log.d(TAG, "onResponse: ${response}")
            }

            override fun onFailure(call: Call<BusinessDetailModel>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.localizedMessage}")
            }

        })
        return businessDetails
    }

    fun getBusinessReviews(id: String?): MutableLiveData<BusinessReviewsModel> {
        val retro=RetroHelper.getInstance().create(BusinessFetcher::class.java)
        retro.getBusinessReviews("Bearer $API_KEY",id!!).enqueue(object : Callback<BusinessReviewsModel>{
            override fun onResponse(
                call: Call<BusinessReviewsModel>,
                response: Response<BusinessReviewsModel>
            ) {
                businessReviews.postValue(response.body())
                Log.d(TAG, "onResponse: review response $response")
            }

            override fun onFailure(call: Call<BusinessReviewsModel>, t: Throwable) {
            }

        })
        return businessReviews
    }

    fun saveReview(userReview: Review, userId: String): MutableLiveData<Boolean> {

        try{
        db.collection("Reviews").document().set(userReview, SetOptions.merge()).addOnCompleteListener {
            if(it.isSuccessful) {
                Log.d(TAG, "saveReview: Review Saved Successfully")
                isReviewSaved.postValue(true)
            }
            else {
                Log.d(TAG, "saveReview: Review saving failed")
                isReviewSaved.postValue(false)
            }
        }
        }catch (e:Exception){
            Log.d(TAG, "saveReview: $e")
            isReviewSaved.postValue(false)
        }
        return isReviewSaved
    }

    fun getUserInfo(userId: String): MutableLiveData<UserModel> {
        db.collection("Users").document(userId).get().addOnCompleteListener {
            if(it.isSuccessful){
                val user:UserModel= UserModel(it.result.get("username").toString(),it.result.getString("email").toString()
                    ,null,userId, it.result.get("userImage") as String?)
                userInfo.postValue(user)
            }
        }
        return userInfo
    }

    fun getBusinessReviewsFromDatabase(businessId: String?): MutableLiveData<List<Review>> {
        val userReviews= ArrayList<Review>(1)
        db.collection("Reviews").whereEqualTo("id",businessId).addSnapshotListener { value, error ->
            userReviews.clear()
            value?.documents?.forEach {
                val user=it.get("user") as Map<String,Any>
                val review=Review(it.get("id").toString(),it.get("rating").toString().toInt(),it.get("text").toString(),it.get("time_created").toString()
                    ,null
                    ,User(user.get("id").toString(),user.get("image_url").toString(),user.get("name").toString(),null))
                userReviews.add(review)
            }
            dbReviews.postValue(userReviews)
        }
        return dbReviews
    }

    fun addBusinessToFavourites(businessId: String?, userId: String) {
        val favBusiness=hashMapOf( "business_id" to businessId )
        db.collection("Users").document(userId).collection("favourite_businesses").document().set(favBusiness, SetOptions.merge())
    }

    fun removeBusinessFromFavourites(businessId: String?, userId: String) {
        db.collection("Users").document(userId).collection("favourite_businesses")
            .whereEqualTo("business_id", businessId).get().addOnCompleteListener{
                it.result.documents.forEach {
                    db.collection("Users").document(userId).collection("favourite_businesses")
                        .document(it.id).delete()
                }
            }

    }

    fun getUserFavouriteBusinesesIds(userId: String): MutableLiveData<List<String>> {
        val favouriteBusinesses= HashMap<String,String>()

        db.collection("Users").document(userId).collection("favourite_businesses")
            .addSnapshotListener { value, error ->
                favouriteBusinesses.clear()
                value?.documents?.forEach {
                    favouriteBusinesses.put(it.get("business_id").toString(),it.get("business_id").toString())
                    Log.d(TAG, "getUserFavouriteBusinesesIds: ${it.get("business_id")}")
                }
                val favs=ArrayList<String>(1)
                favs.addAll(favouriteBusinesses.values)
                userFavouriteBusinessesIds.postValue(favs)
        }
        return userFavouriteBusinessesIds

    }

    fun getCategoryData(category: String?): MutableLiveData<List<Businesse>> {
        val retro=RetroHelper.getInstance().create(BusinessFetcher::class.java)
        category?.let { retro.search("Bearer $API_KEY", it,"San Francisco, CA","restaurants").enqueue(object : Callback<BusinessModel>{
            override fun onResponse(call: Call<BusinessModel>, response: Response<BusinessModel>) {
                if(response.isSuccessful) searchedBusinesses.postValue(response.body()?.businesses)
            }
            override fun onFailure(call: Call<BusinessModel>, t: Throwable) {
            }


        }) }
        return searchedBusinesses
    }

    fun search(query: String): MutableLiveData<List<Businesse>> {
        val retro=RetroHelper.getInstance().create(BusinessFetcher::class.java)
        if(!query.isEmpty()) {
            retro.search("Bearer $API_KEY", query, "San francisco, CA","restaurants")
                .enqueue(object : Callback<BusinessModel> {
                    override fun onResponse(
                        call: Call<BusinessModel>,
                        response: Response<BusinessModel>
                    ) {
                        Log.d(TAG, "onResponse: search $response")
                        if (response.isSuccessful)
                            searchedBusinesses.postValue(response.body()?.businesses)
                    }

                    override fun onFailure(call: Call<BusinessModel>, t: Throwable) {
                        Log.d(TAG, "onFailure: to search ")
                    }
                })
        }
        return searchedBusinesses
    }

    fun getFavourites(userId: String, favouriteIds: ArrayList<String>): MutableLiveData<List<BusinessDetailModel>> {
        val favorites = ArrayList<BusinessDetailModel>(0)
        val retro = RetroHelper.getInstance().create(BusinessFetcher::class.java)
        favouriteIds.forEach {
            Log.d(TAG, "getFavourites: loop")
            retro.getBusiness("Bearer $API_KEY", it)
                .enqueue(object : Callback<BusinessDetailModel> {
                    override fun onResponse(call: Call<BusinessDetailModel>, response: Response<BusinessDetailModel>) {
                        response.body()?.let { it1 -> favorites.add(it1) }
                        Log.d(TAG, "getFavourites: ")
                        Log.d(TAG, "onResponse: ${response}")
                        userFavouritesBusinesses.postValue(favorites)
                    }

                    override fun onFailure(call: Call<BusinessDetailModel>, t: Throwable) {
                        Log.d(TAG, "onFailure: ${t.localizedMessage}")
                    }

                })
        }
        return userFavouritesBusinesses

    }

    fun storeUserProfilePic(userId: String, bitmap: Bitmap?, filePath: Uri?) {
        val byteStream=ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,50,byteStream)
        if (filePath != null) {
            dbStorage.reference.child("User Profile Images/$userId profile_pic").putFile(filePath).addOnSuccessListener {
                Log.d(TAG, "storeUserProfilePic: Profile pic is upload successfully")
                dbStorage.reference.child("User Profile Images/$userId profile_pic").downloadUrl.addOnSuccessListener {
                    Log.d(TAG, "storeUserProfilePic: $it")
                    db.collection("Users").document(userId).update("userImage", it)
                }
            }
        }
    }
    fun getUserProfilePic(userId: String): MutableLiveData<Uri?> {
        var userPicUrl:Uri?
        val file=dbStorage.reference.child("User Profile Images/$userId profile_pic").downloadUrl
            .addOnSuccessListener {
            userPicUrl=it
            userProfilePic.postValue(userPicUrl)
        }.addOnFailureListener{
            userProfilePic.postValue(null)
        }
        return userProfilePic
    }

    fun getUserReviewedBusinesses(userId: String): MutableLiveData<List<BusinessDetailModel>> {
        val busIds= HashMap<String,String>()
        db.collection("Reviews").whereEqualTo("url",userId).get().addOnCompleteListener {
            Log.d(TAG, "getUserReviewedBusinesses: $userId ${it.result.size()}")
            it.result.forEach {
                val bus_id=it.get("id").toString()
                busIds.put(bus_id,bus_id)
            }
            getFavourites(userId, ArrayList(busIds.values))

        }
        return getFavourites(userId,ArrayList(busIds.values))
    }

    fun followUser(userId: String, friendId: String) {
        val userFollows=FollowingModel(userId,friendId)
        db.collection("UserFollowings").document().set(userFollows, SetOptions.merge()).addOnSuccessListener {
            Log.d(TAG, "followUser: $userId is now following $friendId")
        }.addOnFailureListener {
            Log.d(TAG, "followUser: $userId is failed to follow $friendId")
        }
    }
    fun getFollowersIds(userId:String): MutableLiveData<List<String>> {
        val followersIds=ArrayList<String>(1)
        db.collection("UserFollowings").whereEqualTo("follows",userId).addSnapshotListener { value, error ->
            value?.documents?.forEach {
                followersIds.add(it.get("follower").toString())
            }
            followerIds.postValue(followersIds)
        }
        return followerIds
    }
    fun isUserFollowing(userId: String, friendId: String): MutableLiveData<Boolean> {
        db.collection("UserFollowings").whereEqualTo("follower", userId)
            .whereEqualTo("follows", friendId).get()
            .addOnSuccessListener {
                Log.d(TAG, "isUserFollowing: ${it.documents.size}")
                if(it.documents.size>0) {
                    IsUserFollowing.postValue(true)
                }
                else
                    IsUserFollowing.postValue(false)
            }
        return IsUserFollowing
    }
    fun unfollowUser(userId: String, friendId: String) {
        db.collection("UserFollowings").whereEqualTo("follower", userId)
            .whereEqualTo("follows", friendId).get()
            .addOnSuccessListener {
                it.documents.forEach {
                    db.collection("UserFollowings").document(it.id).delete().addOnSuccessListener {
                        Log.d(TAG, "unfollowUser: $userId unfollows $friendId  (deletion is complete)")
                    }.addOnFailureListener{
                        Log.d(TAG, "unfollowUser: $userId failed unfollow $friendId   (deletion is inComplete)")
                    }
                }
            }
    }

    fun getUserFollowing(userId: String): MutableLiveData<List<String>> {
        val followingsIds=ArrayList<String>(1)
        db.collection("UserFollowings").whereEqualTo("follower",userId).addSnapshotListener { value, error ->
            value?.documents?.forEach {
                followingsIds.add(it.get("follows").toString())
            }
            followingIds.postValue(followingsIds)
        }
        return followingIds
    }

    fun getFriends(friendsListIds: ArrayList<String>): MutableLiveData<List<UserModel>> {
        val Users=ArrayList<UserModel>(1)
        friendsListIds.forEach {
            db.collection("Users").document(it).get().addOnCompleteListener {
                if(it.isSuccessful){
                    val user = UserModel(
                        it.result.get("username").toString(),
                        it.result.get("email").toString(),
                        null,
                        it.result.get("userRef").toString(),
                        it.result.get("userImage").toString()
                    )
                    Users.add(user)
                    AllFriends.postValue(Users)
                }
            }
        }
        return AllFriends


    }


}