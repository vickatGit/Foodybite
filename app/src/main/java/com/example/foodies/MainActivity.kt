package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    companion object{
        val LOGIN_SHARED_PREFERENCES="login_ shared_preferences"
        val USER_REFERENCE="user_reference"
        val USER_ID_BRIDGE="user_id_message_passer"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userRef=getSharedPreferences(LOGIN_SHARED_PREFERENCES, MODE_PRIVATE).getString(USER_REFERENCE,null)
        if(userRef==null) {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        else{
            val intent=Intent(this,HomeActivity::class.java)
            intent.putExtra(USER_ID_BRIDGE,userRef)
            startActivity(intent)
        }

    }
}