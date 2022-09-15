package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {

    private lateinit var signup:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialise()
        signup.setOnClickListener {
            startActivity(Intent(this,Signup_Activity::class.java))
        }
    }

    private fun initialise() {
        signup=findViewById(R.id.new_account)
    }
}