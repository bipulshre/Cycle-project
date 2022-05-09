package com.example.cycleproject.auth.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.cycleproject.R
import com.example.cycleproject.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import android.content.Intent
import com.example.cycleproject.MainActivity
import com.example.cycleproject.auth.AuthActivity
import com.example.cycleproject.dashboard.DashboardActivity


class RegisterFragment : Fragment() {

    companion object {
        fun newInstance() = RegisterFragment()
    }

    private var _binding: RegisterFragmentBinding?=null
    private val binding get() = _binding!!
    private lateinit var viewModel: RegisterViewModel

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mAuth= FirebaseAuth.getInstance()

        _binding = RegisterFragmentBinding.inflate(inflater, container, false)

        binding.textViewLoginNow.setOnClickListener { view->
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }


        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        binding.buttonRegister.setOnClickListener {
            val email= binding.editTextTextEmailAddress.text.toString().trim()
            val password = binding.editTextTextPassword.text.toString().trim()

            if(email.isEmpty()){
                binding.editTextTextEmailAddress.error="Email requred"
                binding.editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }

            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.editTextTextEmailAddress.error="Valid Email requred"
                binding.editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }

            if(password.isEmpty()||password.length<6){
                binding.editTextTextPassword.error="6 character password needed"
                binding.editTextTextPassword.requestFocus()
                return@setOnClickListener
            }
            registerUser(email,password)
        }

        // TODO: Use the ViewModel
    }

    private fun registerUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                task->
                if(task.isSuccessful){
                    val i = Intent(activity, DashboardActivity::class.java).apply {
                        //for not to see the login and logout screen on backpress
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(i)
                }
            }

    }

}