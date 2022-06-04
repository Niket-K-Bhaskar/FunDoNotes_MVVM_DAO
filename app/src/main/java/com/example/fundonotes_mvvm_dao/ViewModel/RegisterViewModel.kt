package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonotes_mvvm_dao.Model.AuthListener
import com.example.fundonotes_mvvm_dao.Model.User
import com.example.fundonotes_mvvm_dao.Model.UserAuthService

class RegisterViewModel(private val userAuthService: UserAuthService) : ViewModel() {
        private var _UserRegisterStatus = MutableLiveData<AuthListener>()
        val userRegisterStatus = _UserRegisterStatus as LiveData<AuthListener>

        fun registerUser(user: User){
                userAuthService.userRegister(user){
                        if(it.status){
                                _UserRegisterStatus.value = it
                        }
                }
        }
}