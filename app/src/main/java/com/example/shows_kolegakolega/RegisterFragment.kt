package com.example.shows_kolegakolega

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.shows_kolegakolega.databinding.RegistrationFragmentLayoutBinding
import com.example.shows_kolegakolega.networking.ApiModule.initRetrofit

class RegisterFragment: Fragment() {

    companion object {
        private const val SUCCESFUL_REGISTRATION = "succ_reg"
        private const val PASSWORD_MIN_LENGTH = 5
    }

    private var _binding: RegistrationFragmentLayoutBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegistrationFragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.getPreferences(Context.MODE_PRIVATE)?.let { initRetrofit(it) }
        viewModel.getRegistrationResultLiveData().observe(this.viewLifecycleOwner) {succes ->
            if(succes){
                Toast.makeText(this.context, "USPJEŠNA REGISTRACIJA", Toast.LENGTH_SHORT).show()
                with(activity?.getPreferences(Context.MODE_PRIVATE)?.edit()){
                    this?.putBoolean(SUCCESFUL_REGISTRATION, true)
                    this?.commit()
                }
                findNavController().navigate(R.id.register_to_login)
            } else {
                Toast.makeText(this.context, "NIJE USPJEŠNA REGISTRACIJA", Toast.LENGTH_SHORT).show()
            }

        }

        binding.apply {
            registerButton.setOnClickListener {
                binding.email.error = null
                if(validateEmail(email.editText?.text.toString())){
                    viewModel.register(email.editText?.text.toString(),
                        password.editText?.text.toString(),
                        repeatPassword.editText?.text.toString())
                }else {
                    binding.email.error = "Invalid email!"
                }
            }
        }

        initCheckEmailPassword()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

                binding.registerButton.isEnabled = emailInput.isNotEmpty() && passwordInput.length > PASSWORD_MIN_LENGTH
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

                binding.registerButton.isEnabled = emailInput.isNotEmpty() && passwordInput.length > PASSWORD_MIN_LENGTH
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

}