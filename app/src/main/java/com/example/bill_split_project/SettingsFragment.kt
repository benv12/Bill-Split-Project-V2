package com.example.bill_split_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bill_split_project.databinding.FragmentInviteBinding
import com.example.bill_split_project.databinding.FragmentSettingsBinding


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingActivity()
    }

    private fun settingActivity(){
        binding.btnLogOut.setOnClickListener{
            val intent = Intent(requireActivity(),MainActivity::class.java)
            startActivity(intent)
        }
    }


}