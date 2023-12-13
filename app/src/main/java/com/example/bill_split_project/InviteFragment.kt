package com.example.bill_split_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SearchView
import com.example.bill_split_project.databinding.FragmentInviteBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val TAG = "InviteFragment"
class InviteFragment : Fragment() {

    private lateinit var binding: FragmentInviteBinding
    private var counter = 0
    private lateinit var appDb: AppDatabase
    private lateinit var sendUsername: String

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentInviteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDb = AppDatabase.getDatabase(requireActivity())
        inviteActivity()


    }
    private fun retrieveData(){
        arguments?.let{
            val username = it.getString("passUsername")
            sendUsername = username.toString()
            binding.inviteUsername.text = username.toString()

        }
    }

    private fun inviteActivity(){
        retrieveData()
        listView()

    }
    private fun listView(){
        GlobalScope.launch {
            val temp: Array<String> = appDb.userDao().getUsername()
            if(temp!=null){
                val user = Array(temp.size){""}
                for(i in 0..user.size-1){
                    user[i] = temp[i]
                }
               val userArray = Array(temp.size) { "" }

                binding.myListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE
                binding.myListView.isVerticalScrollBarEnabled

                val userAdapter: ArrayAdapter<String> = ArrayAdapter(
                    requireActivity(),
                    android.R.layout.simple_list_item_multiple_choice,
                    user
                )

                binding.myListView.adapter = userAdapter

                binding.myListView.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
                    val isChecked = binding.myListView.isItemChecked(position)
                    val selectedItem: String = user[position]
                    //sendFriendRequest(sendUsername, selectedItem)
                    if(isChecked==false){
                        //userArray.remove(selectedItem)
                        counter--
                    }
                    userArray[counter] = selectedItem
                    if(isChecked==true){
                        counter++
                    }
                })
                add(userArray)



                binding.mySearchView.setOnQueryTextListener(object :
                    SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        binding.mySearchView.clearFocus()
                        if (user.contains(query)) {
                            userAdapter.filter.filter(query)
                        }
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        userAdapter.filter.filter(newText)
                        return false
                    }
                })
            }
            }
        }

    private fun add(array: Array<String>){
        val username = binding.inviteUsername.text.toString()
        binding.btnAdd.setOnClickListener{
            for(i in array.indices){
                sendFriendRequest(sendUsername, array[i])
            }
            val intent = Intent(activity, HomeActivity::class.java)
            intent.putExtra("passArray", array)
            intent.putExtra("counter", counter)
            intent.putExtra("passUsername", username)
            startActivity(intent)

        }
    }

    private fun sendFriendRequest(sendUsername: String, receiverUsername: String){

        
        if(sendUsername !=null && receiverUsername !=null){
            val friendRequest= FriendRequest(
                null, sendUsername, receiverUsername
            )
            GlobalScope.launch {
                appDb.userDao().insertFriend(friendRequest)
            }
        }
    }
}