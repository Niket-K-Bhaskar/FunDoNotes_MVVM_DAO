package com.example.fundonotes_mvvm_dao.Model

import android.content.ContentValues
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserAuthService {
    private lateinit var firebaseAuth: FirebaseAuth

    init {
        initService()
    }

    private fun initService() {
        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun userRegister(user: User, listener: (AuthListener) -> Unit) {
        firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Log.d(ContentValues.TAG, "createUserWithEmail:success")
                    listener(AuthListener(true, "User registration successful."))
                    storeUserDetails(user,listener)
                } else {
                    Log.w(ContentValues.TAG, "createUserWithEmail:failure")
                    listener(AuthListener(false, "User registration failed."))
                }
            }
    }
    fun storeUserDetails(user: User, listener: (AuthListener) -> Unit) {
        val uid = firebaseAuth.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(user)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    listener(AuthListener(true, "User data saved"))
                } else {
                    listener(AuthListener(false, "User data save failed."))
                }
            }
    }
    fun userLogin(user: User, listener: (AuthListener) -> Unit){
        firebaseAuth.signInWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    listener(AuthListener(true, "User login successful."))
                }
                else{
                    listener(AuthListener(false, "User login failed."))
                }
            }
    }
    fun forgotPassword(user: User, listener: (AuthListener) -> Unit){
        firebaseAuth.sendPasswordResetEmail(user.email)
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    listener(AuthListener(true, "Password Reset mail sent."))
                }
                else{
                    listener(AuthListener(false, "User password reset failed."))
                }
            }
    }

}