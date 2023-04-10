package com.anilkumawat3104.loginwithfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import com.anilkumawat3104.internship1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class login_signup : AppCompatActivity() {

    private lateinit var fogot : TextView
    private lateinit var sign : RelativeLayout
    private lateinit var loginB : RelativeLayout
    private lateinit var email : String
    private lateinit var password : String
    private lateinit var loginemail : EditText
    private lateinit var loginpass : EditText
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)
        supportActionBar?.hide()
        fogot = findViewById(R.id.fogot)
        sign = findViewById(R.id.sign)
        loginB = findViewById(R.id.loginB)
        loginemail = findViewById(R.id.loginemail)
        loginpass = findViewById(R.id.loginpass)
        firebaseAuth = FirebaseAuth.getInstance()
       auth()
//        In this example, we're querying the my_data node in the Firebase database using a ValueEventListener. The onDataChange() method is called when the data is retrieved from the database. Inside this method, we're checking if the DataSnapshot exists and has any children nodes. If there are no children nodes, the database is considered empty.
//
//        Note that we're using addListenerForSingleValueEvent() to listen for a single event rather than continuously listening for changes to the data. This is because we're only interested in checking if the database is empty once. If you want to continuously listen for changes to the data, you can use addValueEventListener() instead.

        fogot.setOnClickListener {
            val intent = Intent(this, forgot::class.java)
            startActivity(intent)
        }
        sign.setOnClickListener{
            val intent = Intent(this, signin::class.java)
            startActivity(intent)
        }
        loginB.setOnClickListener{
            email = loginemail.text.toString()
            password = loginpass.text.toString()

            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(this,"Please enter the email and password", Toast.LENGTH_SHORT)
            }
            else{
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this){
                    if(it.isSuccessful){
                        accountVerification()
                    }
                    else{
                        Toast.makeText(this,"Account does not exist", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun auth(){
        var firebaseUser = firebaseAuth.currentUser
        val    databaseRef = FirebaseDatabase.getInstance().getReference("users").child(firebaseAuth.uid.toString())
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists() && snapshot.childrenCount > 0&&firebaseUser!=null) {
                    // Data is present in the database
                    finish()
                    val intent = Intent(this@login_signup, Feed::class.java)
                    startActivity(intent)
                } else if(firebaseUser!=null) {
                    // Database is empty
                    val intent = Intent(this@login_signup, userInfo::class.java)
                    startActivity(intent)
                    finish()
                }

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
    private fun accountVerification(){
        var firebaseUser = firebaseAuth.currentUser
        if(firebaseUser!!.isEmailVerified==true){
            Toast.makeText(this,"Logged In", Toast.LENGTH_SHORT)
           auth()
        }
        else{
            Toast.makeText(this,"Verify your email", Toast.LENGTH_SHORT)
            firebaseAuth.signOut()
        }
    }
}