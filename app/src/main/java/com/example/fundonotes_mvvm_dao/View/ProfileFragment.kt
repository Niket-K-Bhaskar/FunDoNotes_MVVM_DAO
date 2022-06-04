package com.example.fundonotes_mvvm_dao.View

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.fundonotes_mvvm_dao.R
import com.example.fundonotes_mvvm_dao.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : DialogFragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var rootdatabase: DatabaseReference
    private lateinit var binding: FragmentProfileBinding
    private lateinit var imageUri: Uri
    private lateinit var uploadImagerUrl: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance()
        val uid:String = firebaseAuth.currentUser?.uid.toString()

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        readData(uid)
        binding.selectImageBtn.setOnClickListener{
            selectImage()
        }

        return view
    }
    companion object {
        const val TAG = "Dashboar Profile Dialog"
        fun newInstance(): ProfileFragment {
            return ProfileFragment()
        }
    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    private fun selectImage() {
        val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(openGalleryIntent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        firebaseAuth = FirebaseAuth.getInstance()

        val uid:String = firebaseAuth.currentUser?.uid.toString()

        if(requestCode == 100 && resultCode == Activity.RESULT_OK){
            imageUri = data?.data!!
            uploadImage(uid)
        }
    }
    private fun readData(uid:String){
        rootdatabase = FirebaseDatabase.getInstance().getReference("Users")
        rootdatabase.child(uid).get().addOnSuccessListener {
            if(it.exists()){
                val name = it.child("name").value
                val email = it.child("email").value
                var imageURL= it.child("image").value
                val uid = it.child("uid").value
                val emailTv : TextView = binding.profileFragmentEmailTV
                val nameTv : TextView = binding.profileFragmentNameTV
                val imageIV: ImageView = binding.displayPictureIV

                emailTv.text = email.toString()
                nameTv.text = name.toString()
                Glide.with(this)
                    .load(imageURL)
                    .into(imageIV)
            }
            else{
                Toast.makeText(context, "user does not exist", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener(){
            Toast.makeText(context, "Data read Fail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadImage(uid: String) {
        val fileName:String = "profileimage"
        val storageReference = FirebaseStorage.getInstance().getReference("images/$uid/$fileName")
        rootdatabase = FirebaseDatabase.getInstance().getReference("Users")
        storageReference.putFile(imageUri).
        addOnSuccessListener {
            storageReference.downloadUrl.addOnCompleteListener {
                if(it.isSuccessful){
                    it.addOnSuccessListener {
                        uploadImagerUrl = it.toString()
                        val hashMap: HashMap<String, Any?> = HashMap()
                        hashMap["image"] = uploadImagerUrl
                        rootdatabase.child(uid).updateChildren(hashMap).addOnSuccessListener {
                            Toast.makeText(activity, "Database updated", Toast.LENGTH_LONG).show()
                            readData(uid)
                        }.addOnFailureListener(){
                            Toast.makeText(activity, "Database update failed", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }.addOnFailureListener{
            Toast.makeText(activity,"upload failed", Toast.LENGTH_LONG).show()
        }
    }



}