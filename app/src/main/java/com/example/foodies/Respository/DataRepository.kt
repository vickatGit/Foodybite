package com.example.foodies.Respository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.foodies.Models.BusinessModel
import com.example.foodies.Models.Businesse
import com.example.foodies.Models.UserModel
import com.example.foodies.Networking.BusinessFetcher
import com.example.foodies.Networking.RetroHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DataRepository {
    private val API_KEY="cfo72-p8TKk8JVCClHts2-F2aLDHmaitKVgmwW47YsXUZMpuos_hWoM_0iESLi7mmelKnQeVZLLoEp3eDK7pPO-v5m1AMxIra57QJ6LgdYRfbvejxnx6gWlWXXccY3Yx"

    private val db=FirebaseFirestore.getInstance()
    private val isEmailAlreadyExists:MutableLiveData<Boolean> = MutableLiveData()
    val isUserExist:MutableLiveData<String> = MutableLiveData()
    private val isUserCreated:MutableLiveData<String> = MutableLiveData()

    val popularRestaurants:MutableLiveData<List<Businesse>> = MutableLiveData()

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
            .getPopularRestaurants("Bearer $API_KEY","San Francisco, CA","indpak", arrayOf("hot_and_new"))
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
}