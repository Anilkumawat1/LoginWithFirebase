package com.anilkumawat3104.loginwithfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.anilkumawat3104.internship1.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDateTime

class userInfo : AppCompatActivity() {
    private val REQUEST_IMAGE_PICK = 1
    private lateinit var submit : RelativeLayout
    private lateinit var name : EditText
    private lateinit var about : EditText
    private lateinit var auth : FirebaseAuth
    private lateinit var image : ImageView
    private lateinit var databaseRef : DatabaseReference
    var imageUri : Uri? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        submit = findViewById(R.id.submit)
        name = findViewById(R.id.name)
        about = findViewById(R.id.About)
        image = findViewById(R.id.image)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("users")
        image.setOnClickListener{
            pickImage()
        }

        submit.setOnClickListener {
            var getname : String = name.text.toString()
            var getabout : String = about.text.toString()
            Toast.makeText(this, imageUri.toString(), Toast.LENGTH_SHORT).show()
            if (!getname.isEmpty() && !getabout.isEmpty()) {
                var firebaseUser = auth.currentUser
                val values = hashMapOf<String, String>()
                values.put("Date", LocalDateTime.now().toString())
                values.put("Name", getname)
                values.put("About", getabout)
                values.put("IsDataAdded", "True")
                val storage = FirebaseStorage.getInstance()
                val storageRef = storage.reference
                val imageRef = storageRef.child("images/${imageUri!!.lastPathSegment}")

                imageRef.putFile(imageUri!!)
                    .addOnSuccessListener { taskSnapshot ->
//                Log.d(TAG, "Image upload successful: ${taskSnapshot.metadata?.path}")
//                 Toast.makeText(this,"adsf",Toast.LENGTH_SHORT).show()
                        // Get the download URL of the uploaded image
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            // Store the image metadata in Firestore
                            values.put("uri", uri.toString())
                            databaseRef.child(auth.uid.toString()).setValue(values).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(this, Feed::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                                }

                            }
//                            val db =  FirebaseDatabase.getInstance().getReference("users")
//                            db.child(userId.uid.toString()).child(key1.toString()).push()
//                                .setValue(uri.toString())
//                                .addOnSuccessListener { documentReference ->
////                            Log.d(TAG, "Image metadata stored in Firestore with ID: ${documentReference.id}")
//                                }
//                                .addOnFailureListener {
////                            Log.e(TAG, "Error storing image metadata in Firestore", e)
//                                }
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this,exception.toString(),Toast.LENGTH_SHORT).show()
//                Log.e(TAG, "Image upload failed", exception)
                    }

            } else {
                Toast.makeText(this, "Fill the details", Toast.LENGTH_SHORT).show()

            }
        }
    }

    // Request code for the image picker

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            if(imageUri!=null){
                image.setImageURI(imageUri)
            }
            // Do something with the image URI, such as loading it into an ImageView
        }
    }

}