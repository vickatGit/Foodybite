package com.example.foodies

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var splashImage:ImageView
    private lateinit var splashText:TextView
    private lateinit var context:Context

    companion object{
        val LOGIN_SHARED_PREFERENCES="login_ shared_preferences"
        val USER_REFERENCE="user_reference"
        val USER_ID_BRIDGE="user_id_message_passer"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initialise()
        this.context=this
        val userRef=getSharedPreferences(LOGIN_SHARED_PREFERENCES, MODE_PRIVATE).getString(USER_REFERENCE,null)
        val imageAnimation=AnimationUtils.loadAnimation(this,R.anim.zoom_in_anim)
        val textAnimation=AnimationUtils.loadAnimation(this,R.anim.fade_out_anim)
        splashImage.startAnimation(imageAnimation)
        imageAnimation.setAnimationListener(object:Animation.AnimationListener{
            override fun onAnimationStart(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                splashText.startAnimation( textAnimation)
                textAnimation.setAnimationListener(object:Animation.AnimationListener{
                    override fun onAnimationStart(animation: Animation?) {

                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        if(userRef==null) {
                            startActivity(Intent(context, LoginActivity::class.java))
                            finish()
                        }
                        else{
                            val intent=Intent(context,HomeActivity::class.java)
                            intent.putExtra(USER_ID_BRIDGE,userRef)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation?) {

                    }

                })
            }

            override fun onAnimationRepeat(animation: Animation?) {

            }

        })


    }
    private fun initialise(){
        splashImage=findViewById(R.id.splash_image)
        splashText=findViewById(R.id.splash_text)
        splashText.visibility= View.INVISIBLE
    }
}