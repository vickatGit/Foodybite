package com.example.foodies.Respository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import at.favre.lib.crypto.bcrypt.BCrypt
import com.example.foodies.Models.UserModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DataRepository {

    private val db=FirebaseFirestore.getInstance()
    private val isEmailAlreadyExists:MutableLiveData<Boolean> = MutableLiveData()
    private val isUserExist:MutableLiveData<String> = MutableLiveData()
    private val isUserCreated:MutableLiveData<String> = MutableLiveData()

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
}