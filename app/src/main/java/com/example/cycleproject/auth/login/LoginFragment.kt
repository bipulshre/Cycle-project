package com.example.cycleproject.auth.login

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.cycleproject.R
import com.example.cycleproject.dashboard.DashboardActivity
import com.example.cycleproject.databinding.LoginFragmentBinding
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: LoginViewModel

    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)

        mAuth= FirebaseAuth.getInstance()

        binding.textViewRegisterNow.setOnClickListener{view: View->
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)

        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        binding.buttonLogin.setOnClickListener {
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
            
            loginUser(email, password)

    // TODO: Use the ViewModel

    }

}

    private fun loginUser(email: String, password: String) {
        binding.progressbar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
            binding.progressbar.visibility= View.GONE
            if (task.isSuccessful) {
                val i = Intent(activity, DashboardActivity::class.java).apply {
                    //for not to see the login and logout screen on backpress
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(i)

            }
        }
        }
    }
