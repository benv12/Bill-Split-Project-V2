package com.example.bill_split_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.bill_split_project.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import androidx.fragment.app.FragmentTransaction

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var appDb : AppDatabase

    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = AppDatabase.getDatabase(this)



        binding.btnLogin.setOnClickListener{
            logIn()

        }


        binding.btnClear.setOnClickListener{
            deleteAll()
        }

        binding.btnSignUpMain.setOnClickListener{
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }


    }



    private fun sendData(){
        username = binding.newUsername.text.toString()

            val intent = Intent(this,HomeActivity::class.java).also{
                it.putExtra("passUsername", username)
                startActivity(it)
            }
    }

    private fun mainActivity(){

        val username = binding.newUsername.text.toString()
        val password = binding.etPassword.text.toString()

        if(username.isNotEmpty()&&password.isNotEmpty()){

            val user = User(
                null,username,password
            )

            GlobalScope.launch(Dispatchers.IO){

                appDb.userDao().insert(user)

            }
            sendData()

            binding.newUsername.text.clear()
            binding.etPassword.text.clear()

        }
    }

    private fun logIn(){

        GlobalScope.launch{
            val usernameArray: Array<String> = appDb.userDao().getUsername()
            val passwordArray: Array<String> = appDb.userDao().getPassword()
            if(usernameArray!=null&&passwordArray!=null){
                val username1 = binding.newUsername.text.toString()
                val password = binding.etPassword.text.toString()

                for(i in 0..usernameArray.size-1){
                    if(usernameArray[i].equals(username1)&&passwordArray[i].equals(password)) {
                        sendData()
                    }
                }
            }
        }
    }


    private fun deleteAll(){
        GlobalScope.launch{
            appDb.userDao().deleteAll()
            appDb.userDao().deleteAllFriends()
        }
    }
}