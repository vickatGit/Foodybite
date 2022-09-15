package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodies.ViewModels.LoginViewModel
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity() {

    private lateinit var signup:Button
    private lateinit var login:Button
    private lateinit var email:EditText
    private lateinit var emailContainer:TextInputLayout
    private lateinit var password:EditText
    private lateinit var passwordContainer:TextInputLayout
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginProgress:ProgressBar
    private lateinit var loginError:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        viewModel=ViewModelProvider(this).get(LoginViewModel::class.java)
        initialise()
        addInputListners()
        login.setOnClickListener {
            if(isAllFieldsAreFilled()){
                loginProgress.visibility= View.VISIBLE
                viewModel.isUserExist(email.text.toString(),password.text.toString())?.observe(this, Observer {
                    if(it!=null){
                        loginProgress.visibility= View.INVISIBLE
                        val intent=Intent(this,HomeActivity::class.java)
                        intent.putExtra("userId",it)
                        startActivity(intent)
                    }else{
                        loginError.visibility=View.VISIBLE
                        loginProgress.visibility= View.INVISIBLE
                        Toast.makeText(this,"Incorrect information",Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        signup.setOnClickListener {
            startActivity(Intent(this,Signup_Activity::class.java))
        }
    }

    private fun isAllFieldsAreFilled(): Boolean {
        if(!email.text.isEmpty() && !password.text.isEmpty()){
            return true
        }else{

            if(password.text.isEmpty()){ passwordContainer.isErrorEnabled=true
                passwordContainer.error="this is Required"
            }
            if(email.text.isEmpty()){ emailContainer.isErrorEnabled=true
                emailContainer.error="this is Required"
            }
            return false
        }
    }
    private fun addInputListners(){
        password.addTextChangedListener { passwordContainer.isErrorEnabled=false
            loginError.visibility=View.INVISIBLE
        }
        email.addTextChangedListener { emailContainer.isErrorEnabled=false
            loginError.visibility=View.INVISIBLE
        }
    }

    private fun initialise() {
        signup=findViewById(R.id.new_account)
        login=findViewById(R.id.login)
        loginProgress=findViewById(R.id.progress)
        email=findViewById(R.id.email)
        emailContainer=findViewById(R.id.email_container)
        password=findViewById(R.id.password)
        passwordContainer=findViewById(R.id.password_container)
        loginError=findViewById(R.id.login_error)

    }
}