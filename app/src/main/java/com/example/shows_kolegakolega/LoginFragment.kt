package com.example.shows_kolegakolega

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shows_kolegakolega.databinding.ActivityLoginBinding

class LoginFragment : Fragment() {

    companion object {
        private const val PASSWORD_MIN_LENGTH = 5
        private const val EMAIL = "EMAIL"
        private const val REMEMBER_ME = "Remember_me"
        private const val SUCCESFUL_REGISTRATION = "succ_reg"
    }

    private var _binding: ActivityLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivityLoginBinding.inflate(inflater, container, false)
        checkIfSuccesfulRegistartion()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSignInResultLiveData().observe(viewLifecycleOwner){ succes ->
            if(succes){
                findNavController().navigate(R.id.action_login_to_shows)
            }else{
                Toast.makeText(this.context, "Prijava nije uspjela", Toast.LENGTH_SHORT).show()
            }

        }
        checkIfSuccesfulRegistartion()
        initAutoLogin()
        initCheckEmailPassword()
        initLoginButton()
        initRegisterButton()

    }

    private fun checkIfSuccesfulRegistartion() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val succRegistration = prefs?.getBoolean(SUCCESFUL_REGISTRATION, false)
        if(succRegistration == true){
            binding.login.text = "Registration successful!"
            binding.registration.isVisible = false
        }
    }

    private fun initRegisterButton() {
        binding.registration.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
    }

    private fun initAutoLogin() {
        val prefs = activity?.getPreferences(Context.MODE_PRIVATE)
        val alreadyLogedIn = prefs?.getBoolean(REMEMBER_ME, false)
        if(alreadyLogedIn == true){
            findNavController().navigate(R.id.action_login_to_shows)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun initLoginButton() {
        binding.loginbtn.setOnClickListener {
            binding.email.error = null

            val email = binding.email.editText?.text.toString()

            if(validateEmail(email)) {
                isRememberMeChecked()
                activity?.getPreferences(Context.MODE_PRIVATE)?.let { it1 ->
                    viewModel.signIn(binding.email.editText?.text.toString(),
                        binding.password.editText?.text.toString(),
                        it1
                    )
                }
            }else {
                binding.email.error = "Invalid email!"
            }
        }
    }

    private fun isRememberMeChecked() {
        with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()){
            if(binding.chechboxRememberMe.isChecked){
                this?.putBoolean(REMEMBER_ME, true)
            }
            this?.apply()
        }

    }

    private fun validateEmail(email: String): Boolean {
        return email.contains(Patterns.EMAIL_ADDRESS.toRegex())
    }

    private fun initCheckEmailPassword(){
        binding.email.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailInput : String = binding.email.editText?.text.toString().trim()
                val passwordInput : String = binding.password.editText?.text.toString().trim()

                binding.loginbtn.isEnabled = emailInput.isNotEmpty() && passwordInput.length > PASSWORD_MIN_LENGTH
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.password.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val emailInput : String = binding.email.editText?.text.toString().trim()
                val passwordInput : String = binding.password.editText?.text.toString().trim()

                binding.loginbtn.isEnabled = emailInput.isNotEmpty() && passwordInput.length > PASSWORD_MIN_LENGTH
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

}