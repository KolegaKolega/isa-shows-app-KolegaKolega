package com.example.shows_kolegakolega

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.shows_kolegakolega.databinding.ActivityLoginBinding
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val PASSWORD_MIN_LENGTH = 5
    }

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initCheckEmailPassword()
        initLoginButton()

    }

    private fun initLoginButton() {
        binding.loginbtn.setOnClickListener {
            binding.email.error = null

            val email = binding.email.editText?.text.toString()
            Log.println(Log.DEBUG,"", email)
            if(validateEmail(email)) {
                val intent = ShowsActivity.buildIntent(this)
                //val intent = WelcomeActivity.buildIntent(this, email)
                startActivity(intent)
            }else {
                binding.email.error = "Invalid email!"
            }
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