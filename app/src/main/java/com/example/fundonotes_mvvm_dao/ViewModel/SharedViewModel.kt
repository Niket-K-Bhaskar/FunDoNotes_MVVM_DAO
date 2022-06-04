package com.example.bridgelabzproject_fundonotes.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fundonotes_mvvm_dao.Model.UserAuthService

class SharedViewModel(userAuthService: UserAuthService) : ViewModel() {
    private var _GoToRegisterPageStatus = MutableLiveData<Boolean>()
    val goToRegisterPageStatus = _GoToRegisterPageStatus as LiveData<Boolean>
    private val _QuerryText = MutableLiveData<String>()
    val querryText : LiveData<String> = _QuerryText

    private var _GoToLoginPageStatus = MutableLiveData<Boolean>()
    val goToLoginPageStatus = _GoToLoginPageStatus as LiveData<Boolean>

    private var _GoToDashboardPageStatus = MutableLiveData<Boolean>()
    val goToDashboardPageStatus = _GoToDashboardPageStatus as LiveData<Boolean>

    fun setGoToDashboardPage(status: Boolean){
        _GoToDashboardPageStatus.value = status
    }
    fun setGoToRegisterPage(status: Boolean){
        _GoToRegisterPageStatus.value = status
    }
    fun setGoToLoginPage(status: Boolean){
        _GoToLoginPageStatus.value = status
    }
    fun setQuerryText(text: String){
        _QuerryText.value = text
    }

}