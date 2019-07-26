package com.example.walklist.views.activities

import android.content.Intent
import android.os.Bundle
import com.example.walklist.R
import com.example.walklist.controllers.UserController
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)

        btAction.setOnClickListener {
            if (isInvalidEntriesInForm()) return@setOnClickListener
            attemptLogin()
        }

        btRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun attemptLogin() {
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()
        UserController.login(email, password, this)
    }

    private fun isInvalidEntriesInForm(): Boolean {
        var result = false
        if (etEmail.text!!.toString() == "") {
            etEmail.error = "Empty email is not allowed"
            result = true
        }
        if (etPassword.text!!.toString() == "") {
            etPassword.error = "Empty password is not allowed"
            result = true
        }
        return result
    }

}
