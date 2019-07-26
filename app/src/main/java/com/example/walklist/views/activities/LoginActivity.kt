package com.example.walklist.views.activities

import android.content.Intent
import android.os.Bundle
import com.grabclone.driver.R
import com.grabclone.driver.managers.UserManager
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
        val mobile = etMobile.text.toString()
        val password = etPassword.text.toString()
        UserManager.login(mobile, password, this)
    }

    private fun isInvalidEntriesInForm(): Boolean {
        var result = false
        if (etMobile.text!!.toString() == "") {
            etMobile.error = "Empty mobile number is not allowed"
            result = true
        }
        if (etPassword.text!!.toString() == "") {
            etPassword.error = "Empty password is not allowed"
            result = true
        }
        return result
    }

}
