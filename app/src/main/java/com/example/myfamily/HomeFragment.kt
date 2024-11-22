package com.example.myfamily

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.Contract

class HomeFragment : Fragment() {

    private val listContacts: ArrayList<ContactModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listMembers = listOf<MemberModel>(
                MemberModel(
                    "Naina",
                    "9th buildind, 2nd floor, maldiv road, manali 9th buildind, 2nd floor",
                    "90%",
                    "220"
                ),
                MemberModel(
                    "Painuly",
                    "10th buildind, 3rd floor, maldiv road, manali 10th buildind, 3rd floor",
                    "80%",
                    "210"
                ),
                MemberModel(
                    "SHMILY",
                    "11th buildind, 4th floor, maldiv road, manali 11th buildind, 4th floor",
                    "70%",
                    "200"
                ),
                MemberModel(
                    "Tae",
                    "12th buildind, 5th floor, maldiv road, manali 12th buildind, 5th floor",
                    "60%",
                    "190"
                ),
        )


        val adapter = MemberAdapter(listMembers)

        val recycler = requireView().findViewById<RecyclerView>(R.id.recycler_member)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = adapter
        Log.d("fetchContact89", "fetchContacts: start karna wale hain")

        Log.d("fetchContact89", "fetchContacts: start hogya hai ${listContacts.size}")
        val inviteAdapter = InviteAdapter(listContacts)
        Log.d("fetchContact89", "fetchContacts: end hogya hai")

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("fetchContact89", "fetchContacts: coroutine start")
            listContacts.addAll(fetchContacts())

            withContext(Dispatchers.Main){
                inviteAdapter.notifyDataSetChanged()
            }

            Log.d("fetchContact89", "fetchContacts: coroutine end ${listContacts.size}")
        }

        val inviteRecycler = requireView().findViewById<RecyclerView>(R.id.recycler_invite)
        inviteRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        inviteRecycler.adapter = inviteAdapter

    }

    private fun fetchContacts() : ArrayList<ContactModel>{

        Log.d("fetchContact89", "fetchContacts: start")
        val cr= requireActivity().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI,null,null,null,null)
        val listContacts:ArrayList<ContactModel> = ArrayList()
        if(cursor!=null && cursor.count>0){
            while(cursor!=null && cursor.moveToNext()){
                val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME))
                val hasPhoneNumber = cursor.getInt(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if(hasPhoneNumber>0){

                    val pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
                        arrayOf(id),
                        ""
                    )

                    if(pCur !=null && pCur.count>0){

                        while(( pCur!=null ) && pCur.moveToNext()){

                            val phoneNum = pCur.getString(pCur.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))

                            listContacts.add(ContactModel(name,phoneNum))
                        }
                        pCur.close()
                    }
                }
            }
            if(cursor!=null){
                cursor.close()
            }
        }
        Log.d("fetchContact89", "fetchContacts: end")
        return listContacts
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}