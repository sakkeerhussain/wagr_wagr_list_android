package com.example.walklist.controllers

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.walklist.api.*
import com.example.walklist.utils.Const
import com.example.walklist.utils.User
import com.example.walklist.views.activities.BaseActivity
import com.example.walklist.views.activities.LoginActivity
import com.example.walklist.views.activities.MainActivity

object UserController {

    fun isLoggedIn(context: Context): Boolean {
        return getToken(context) != ""
    }

    private fun storeToken(token: String, context: Context) {
        SharedPrefController.store(Const.SharedPref.TOKEN, token, context)
    }

    private fun storeUser(user: User, context: Context) {
        SharedPrefController.store(Const.SharedPref.USER, user, context)
    }

    fun getToken(context: Context): String {
        val token = SharedPrefController.get(Const.SharedPref.TOKEN, "", context)
        return token as String
    }

    fun login(email: String, password: String, activity: BaseActivity) {
        val pDialog = ProgressDialog.show(activity, "Loading...", "Attempting to login")
        ApiService.getService(activity).login(LoginReqModel(email = email, password = password))
            .enqueue(object : BaseApiCallback<LoginRespModel>(activity) {

                override fun onSuccess(result: LoginRespModel) {
                    pDialog.dismiss()
                    handleSuccessfulLogin(result.data, activity)
                }

                override fun onError(message: String) {
                    pDialog.dismiss()
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }

            })
    }

    fun register(email: String, firstName: String, password: String, activity: BaseActivity) {

        val user = User(email = email, password = password, firstName = firstName, lastName = "")

        val pDialog = ProgressDialog.show(activity, "Loading...",
            "Attempting to register")
        ApiService.getService(activity).register(user)
            .enqueue(object : BaseApiCallback<LoginRespModel>(activity) {

                override fun onSuccess(result: LoginRespModel) {
                    pDialog.dismiss()
                    handleSuccessfulLogin(result.data, activity)
                }

                override fun onError(message: String) {
                    pDialog.dismiss()
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                }

            })

    }

    private fun handleSuccessfulLogin(data: LoginResp, activity: BaseActivity) {
        storeToken(data.token, activity)
        storeUser(data.user, activity)

        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        activity.startActivity(intent)
    }

    fun logout(context: Context) {
        storeToken("", context)
        storeUser(User("", "", "", ""), context)
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }
}