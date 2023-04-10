package com.anilkumawat3104.loginwithfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anilkumawat3104.internship1.R
import com.google.firebase.database.*

class Feed : AppCompatActivity() {
    private lateinit var databaseRef : DatabaseReference
    private var ItemList = ArrayList<data>()
    private lateinit var adapter: Adapter
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)
        databaseRef = FirebaseDatabase.getInstance().getReference("users")
        recyclerView = findViewById(R.id.recyclerview)
        val layoutManager = LinearLayoutManager(this)
        adapter = Adapter(ItemList)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (user in dataSnapshot.children) {
                    var name : String = ""
                    var about : String = ""
                    var uri : String = ""
                    for(data in user.children) {

                        if (data.key.toString().equals("Name")) {
                            name = data.getValue().toString()
                        } else if (data.key.toString().equals("About")) {
                            about = data.getValue().toString()
                        }
                        else if(data.key.toString().equals("uri")){
                            uri = data.getValue().toString()
                        }
                    }
                    var data = data(name, about, uri)
                    ItemList.add(data)
//                    Toast.makeText(this@images,postSnapshot.getValue().toString(),Toast.LENGTH_SHORT).show()

                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

}