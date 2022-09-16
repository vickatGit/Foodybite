package com.example.foodies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PatternMatcher
import android.os.UserManager
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.foodies.Models.UserModel
import com.example.foodies.ViewModels.SignupViewModel
import com.google.android.material.textfield.TextInputLayout

class Signup_Activity : AppCompatActivity() {

    private lateinit var usernameContainer:TextInputLayout
    private lateinit var username:EditText
    private lateinit var emailContainer:TextInputLayout
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var passwordContainer:TextInputLayout
    private lateinit var confirmPassword:EditText
    private lateinit var confirmPasswordContainer:TextInputLayout
    private lateinit var register:Button
    private lateinit var login:Button
    private lateinit var viewModel:SignupViewModel
    private lateinit var signupProgress:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        initialise()
        addInputListners()
        viewModel=ViewModelProvider(this).get(SignupViewModel::class.java)
        register.setOnClickListener {
            if(isAllFieldsAreFilled()){
                if(isEmailValid(email.text.toString())) {
                    signupProgress.visibility= View.VISIBLE
                    Log.d("tag", "onCreate: email is not valid")
                    viewModel.isEmailAlreadyExists(email.text.toString())?.observe(this, Observer {
                        if(it==true) {
                            emailContainer.isErrorEnabled=true
                            emailContainer.error = "Email Already Exists"
                            signupProgress.visibility= View.INVISIBLE
                        }
                        else{ if (isBothPasswordMatches()) {
                                val user = UserModel(username.text.toString(), email.text.toString(), password.text.toString(), null)
                                viewModel.registerUser(user)?.observe(this, Observer {
                                    val intent=Intent(this,HomeActivity::class.java)
                                    intent.putExtra("userId",it)
                                    val loginEditor=getSharedPreferences(MainActivity.LOGIN_SHARED_PREFERENCES, MODE_PRIVATE).edit()
                                    loginEditor.putString(MainActivity.USER_REFERENCE,it).commit()
                                    startActivity(intent)
                                    signupProgress.visibility= View.INVISIBLE
                                })
                            }else
                                signupProgress.visibility= View.INVISIBLE
                        }
                    })
                }
            }
        }
        login.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
    }


    private fun addInputListners(){
        confirmPassword.addTextChangedListener { confirmPasswordContainer.isErrorEnabled=false }
        password.addTextChangedListener { passwordContainer.isErrorEnabled=false }
        username.addTextChangedListener { usernameContainer.isErrorEnabled=false }
        email.addTextChangedListener { emailContainer.isErrorEnabled=false }
    }
    private fun isBothPasswordMatches(): Boolean {
        if(password.text.toString().equals(confirmPassword.text.toString())){
            return true
        }else{
            passwordContainer.isErrorEnabled=true
            confirmPasswordContainer.isErrorEnabled=false
            passwordContainer.error="different passwords"
            confirmPasswordContainer.error="different passwords"
            return false
        }
    }

    private fun isAllFieldsAreFilled(): Boolean {
        if(!username.text.isEmpty() && !email.text.isEmpty() && !password.text.isEmpty()){
            return true
        }else{

                if(password.text.isEmpty()){ passwordContainer.isErrorEnabled=true
                    passwordContainer.error="this is Required"
                }
                if(confirmPassword.text.isEmpty()){ confirmPasswordContainer.isErrorEnabled=true
                    confirmPasswordContainer.error="this is Required"
                }
                if(email.text.isEmpty()){ emailContainer.isErrorEnabled=true
                    emailContainer.error="this is Required"
                }
                if(username.text.isEmpty())  { usernameContainer.isErrorEnabled=true
                    usernameContainer.error="this is Required"
                }

            return false
        }
    }

    private fun isEmailValid(email: String): Boolean {
        if(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true
        }else{
            emailContainer.isErrorEnabled=true
            emailContainer.error="Email is Not Valid"
            return false
        }
    }

    private fun initialise() {
        usernameContainer=findViewById(R.id.username_container)
        username=findViewById(R.id.username)
        emailContainer=findViewById(R.id.email_container)
        email=findViewById(R.id.email)
        password=findViewById(R.id.password)
        passwordContainer=findViewById(R.id.password_container)
        confirmPassword=findViewById(R.id.confirm_password)
        confirmPasswordContainer=findViewById(R.id.confirm_password_container)
        register=findViewById(R.id.register)
        login=findViewById(R.id.login)
        signupProgress=findViewById(R.id.progress)
    }
}