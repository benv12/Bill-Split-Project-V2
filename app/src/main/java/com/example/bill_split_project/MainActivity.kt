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
        username = binding.newUsername.text.toString()+"#"+binding.etZelleMain.text.toString()

            val intent = Intent(this,HomeActivity::class.java).also{
                it.putExtra("passUsername", username)
                startActivity(it)
            }
    }

    private fun logIn(){

        GlobalScope.launch{
            val usernameArray: Array<String> = appDb.userDao().getUsername()
            val passwordArray: Array<String> = appDb.userDao().getPassword()
            val zelleUsername: Array<String> = appDb.userDao().getZelle()
            if(usernameArray!=null&&passwordArray!=null){
                val username1 = binding.newUsername.text.toString()
                val password = binding.etPassword.text.toString()
                val zelle = binding.etZelleMain.text.toString()

                for(i in usernameArray.indices){
                    if(usernameArray[i]==username1&&passwordArray[i]==password&&zelleUsername[i]==zelle) {
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