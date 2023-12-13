package com.example.bill_split_project

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bill_split_project.databinding.FragmentHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var counter = 0
    private lateinit var appDb: AppDatabase
    private lateinit var receiverUsername: String
    private var recieveRequest: Boolean = false
    private lateinit var senderUsername: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDb = AppDatabase.getDatabase(requireActivity())
        homeActivity()
        if(recieveRequest){
            val message : String? = "$senderUsername invited you to join his party!"
            showFriendRequest(message)
        }
    }

    private fun retrieveData(){

            arguments?.let{
               val username = it.getString("passUsername")
                receiverUsername = username.toString()
                showMessage()
                binding.usernameLabel.text = "Hi, " + username.toString()
            }
    }

    private fun showMessage(){
        GlobalScope.launch {
            val sender: Array<String> = appDb.userDao().getSendUsername()
            val receiver: Array<String> = appDb.userDao().getReceiverUsername()

            for(i in receiver.indices){
                if(receiverUsername == receiver[i]) {
                    senderUsername = sender[i]
                    recieveRequest = true

                }
            }
        }
    }

    private fun getData() {
        val userArray = arguments?.getStringArray("passArray")
        val username = arguments?.getString("passUsername")
        val count = arguments?.getInt("counter")
        if(userArray!=null && count!=null){
            binding.tvParty.setText("Party: $count")
            counter = count
            binding.usernameLabel.setText(username)
            val userAdapter: ArrayAdapter<String> = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_list_item_1,
                userArray
            )

            binding.myListView.adapter = userAdapter
        }
    }

    private fun homeActivity(){
        getData()
        retrieveData()

        binding.mySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.tvTip.text = "$progress%"
                compute()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        binding.etBill.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                compute()
            }
        })
        binding.btn15.setOnClickListener{
            binding.mySeekBar.progress = 15
        }
        binding.btn20.setOnClickListener{
            binding.mySeekBar.progress = 20
        }
        binding.btn25.setOnClickListener{
            binding.mySeekBar.progress = 25
        }
        binding.btn30.setOnClickListener{
            binding.mySeekBar.progress = 30
        }

    }
    private fun compute(){
        if(binding.etBill.text.isEmpty()){
            binding.myTip.text = "$0.0"
            binding.tvBill.text = "$0.0"
            binding.tvTotal.text = "$0.0"
            return
        }
        val bill = binding.etBill.text.toString().toDouble()
        val tip = binding.mySeekBar.progress

        val tipAmount = bill * tip / 100
        val total = (bill + tipAmount)
        var splitBill = (bill+tipAmount)
        if(counter!=0){
            splitBill = (bill+tipAmount)/(counter+1)
        }

        binding.myTip.text = "$%.2f".format(tipAmount)
        binding.tvBill.text = "$%.2f".format(total)
        binding.tvTotal.text = "$%.2f".format(splitBill)
    }

    private fun showFriendRequest(message: String?) {

            val dialog = Dialog(requireActivity())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.layout_custom_dialog)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val tvMessage: TextView = dialog.findViewById(R.id.tvMessage)
            val btnAccept: Button = dialog.findViewById(R.id.btnAccept)
            val btnDecline: Button = dialog.findViewById(R.id.btnDecline)

            tvMessage.text = message

            btnAccept.setOnClickListener {
                val userArray= Array(1){""}
                for(i in userArray.indices){
                    userArray[i]=senderUsername

                }
                val userAdapter: ArrayAdapter<String> = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_1,
                    userArray
                )

                binding.myListView.adapter = userAdapter

                counter = 1
                binding.tvParty.setText("Party: 2")
                dialog.dismiss()
            }
            btnDecline.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }

}