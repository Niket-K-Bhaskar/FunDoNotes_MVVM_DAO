package com.example.fundonotes_mvvm_dao.View

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bridgelabzproject_fundonotes.viewmodel.*
import com.example.fundonotes_mvvm_dao.Model.User
import com.example.fundonotes_mvvm_dao.Model.UserAuthService
import com.example.fundonotes_mvvm_dao.R
import com.example.fundonotes_mvvm_dao.databinding.FragmentLoginBinding
import com.example.fundonotes_mvvm_dao.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {
    private lateinit var binding : FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(UserAuthService())).get(
            LoginViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(
            UserAuthService()
        )
        ).get(SharedViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()
        // Inflate the layout for this fragment
        binding.textView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.button.setOnClickListener {
            loginUser()
            val intent = Intent (activity, DashboardActivity::class.java)
            activity?.startActivity(intent)
        }
        binding.forgotPass.setOnClickListener{
            sendForgotPassEmail()
        }
        return view
    }
    fun loginUser(){
        val email = binding.emailEt.text.toString()
        val pass = binding.passET.text.toString()
        val user = User(email = email, password = pass)

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            loginViewModel.loginUser(user)
            loginViewModel.userLoginStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    sharedViewModel.setGoToDashboardPage(true)
                }
                else{
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    sharedViewModel.setGoToDashboardPage(false)
                    sharedViewModel.setGoToLoginPage(true)
                }
            })
        }
        else {
            Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

        }

    }
    private fun sendForgotPassEmail() {
        var email = ""
        var password = ""
        email = binding.emailEt.text.toString().trim()
        password = binding.passET.text.toString().trim()
        val user = User(email = email, password = password)

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //invalid Email Pattern
            Toast.makeText(activity, "Invalid Email", Toast.LENGTH_LONG).show()
        }
        else if (email.isEmpty()){
            //empty password
            Toast.makeText(activity, "Enter Email", Toast.LENGTH_LONG).show()
        }
        else{
            loginViewModel.forgotPassword(user)
            loginViewModel.ForgotPasswordStatus.observe(viewLifecycleOwner, Observer {
                if(it.status){
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}