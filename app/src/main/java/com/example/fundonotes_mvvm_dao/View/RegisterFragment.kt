package com.example.fundonotes_mvvm_dao.View

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.bridgelabzproject_fundonotes.viewmodel.RegisterViewModel
import com.example.bridgelabzproject_fundonotes.viewmodel.RegisterViewModelFactory
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModel
import com.example.bridgelabzproject_fundonotes.viewmodel.SharedViewModelFactory
import com.example.fundonotes_mvvm_dao.Model.User
import com.example.fundonotes_mvvm_dao.Model.UserAuthService
import com.example.fundonotes_mvvm_dao.R
import com.example.fundonotes_mvvm_dao.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {
    private lateinit var binding : FragmentRegisterBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var sharedViewModel: SharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        val view = binding.root
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(UserAuthService())).get(RegisterViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity(), SharedViewModelFactory(UserAuthService())).get(SharedViewModel::class.java)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.textView.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
        binding.button.setOnClickListener{
            registerUser()
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return view
    }
    fun registerUser(){
        val email = binding.emailEt.text.toString()
        val pass = binding.passET.text.toString()
        val confirmPass = binding.confirmPassEt.text.toString()
        val name = binding.nameEt.text.toString()
        val user = User(name = name, email = email, password = pass, image = "https://upload.wikimedia.org/wikipedia/commons/thumb/3/34/PICA.jpg/900px-PICA.jpg")

        if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
            if (pass == confirmPass) {
                registerViewModel.registerUser(user)
                registerViewModel.userRegisterStatus.observe(viewLifecycleOwner, Observer {
                    if(it.status){
                        Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                        sharedViewModel.setGoToDashboardPage(true)
                    }
                    else{
                        Toast.makeText(context, it.msg,Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(requireContext(), "Password is not matching", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
        }

    }

}
