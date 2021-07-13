package com.example.shows_kolegakolega

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.shows_kolegakolega.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private var emailEditText : EditText? = null
    private var passwordEditText : EditText? = null
    private var loginButton : Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        emailEditText = binding.email.editText
        passwordEditText = binding.password.editText
        loginButton = binding.loginbtn

        initCheckEmailPassword()
        initLoginButton()

    }

    private fun initLoginButton() {
        loginButton?.setOnClickListener {
            binding.email.error = null

            val email = emailEditText?.text.toString()
            Log.println(Log.DEBUG,"", email)
            if(validateEmail(email)) {
                val intent = WelcomeActivity.buildIntent(this, email)
                startActivity(intent)
            }else {
                binding.email.error = "Invalid email!"
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return email.contains("^\\S+@\\S+\$".toRegex())
    }

    private fun initCheckEmailPassword(){
        emailEditText?.addTextChangedListener(textWatcher)
        passwordEditText?.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val eText : String = emailEditText?.text.toString().trim()
            val pText : String = passwordEditText?.text.toString().trim()

            loginButton?.isEnabled = eText.isNotEmpty() && pText.length > 5
        }

        override fun afterTextChanged(s: Editable?) {
        }


    }
}