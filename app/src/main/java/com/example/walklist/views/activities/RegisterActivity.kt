package com.example.walklist.views.activities

import android.content.Intent
import android.os.Bundle
import com.example.walklist.R
import com.example.walklist.controllers.UserController
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(false) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(toolbar)

        btAction.setOnClickListener {
            if (isInvalidEntriesInForm()) return@setOnClickListener
            attemptRegister()
        }

        btLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun attemptRegister() {
        val email = etEmail.text.toString()
        val firstName = etFirstName.text.toString()
        val password = etPassword.text.toString()

        UserController.register(email, firstName, password, this)

    }

    private fun isInvalidEntriesInForm(): Boolean {
        var result = false
        if (etFirstName.text.toString() == "") {
            etFirstName.error = "Empty name is not allowed"
            result = true
        }
        if (etEmail.text!!.toString() == "") {
            etEmail.error = "Empty email is not allowed"
            result = true
        }
        if (etPassword.text!!.toString() == "") {
            etPassword.error = "Empty password is not allowed"
            result = true
        }
        if (etPasswordConfirm.text!!.toString() == "") {
            etPasswordConfirm.error = "Empty confirm password is not allowed"
            result = true
        }
        if (etPasswordConfirm.text!!.toString() != etPassword.text!!.toString()) {
            etPasswordConfirm.error = "Confirm password is not matching with password"
            result = true
        }
        return result
    }

}
