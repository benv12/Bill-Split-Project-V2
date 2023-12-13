package com.example.bill_split_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.bill_split_project.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(HomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {

            when(it.itemId){
                R.id.home_nav -> {
                    replaceFragment(HomeFragment())

                }
                R.id.add_nav ->{
                    replaceFragment(AddFragment())

                }
                R.id.invite_nav -> {

                    replaceFragment(InviteFragment())
                }
                R.id.setting_nav -> {

                    replaceFragment(SettingsFragment())
                }

                else ->{

                }
            }
            true
        }
    }
    private fun getData(fragment: Fragment){
        val userArray = intent.getStringArrayExtra("passArray")
        val username = intent.getStringExtra("passUsername")
        val counter = intent.getIntExtra("counter", 0)
        val party = intent.getIntExtra("passParty", 0)
        if(userArray!=null){
            val bundle = Bundle()
            bundle.putStringArray("passArray", userArray)
            bundle.putInt("counter", counter)
            bundle.putString("passUsername", username)
            fragment.arguments = bundle
        }
    }

    private fun passData(fragment: Fragment){
        val temp = intent.getStringExtra("passUsername")
        val bundle = Bundle().apply{
            val username = temp.toString()
            putString("passUsername", username)
        }

        fragment.arguments = bundle

    }

    private fun replaceFragment(fragment: Fragment){
        passData(fragment)
        getData(fragment)
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

}