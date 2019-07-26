package com.example.walklist.api

import android.content.Context
import com.example.walklist.controllers.UserController
import com.grabclone.driver.api.BaseRespModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseApiCallback<T>(val mContext: Context): Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        val result = response.body() as? BaseRespModel

        // Checking for non 200 response from server
        if (response.isSuccessful && result != null) {

            // Handling expected error case
            if (result.status == "ok") {
                onSuccess(result as T)
            } else {
                onError(result.message)
            }
        } else if (response.code() == 401) {
            UserController.logout(mContext)
        } else {
            onError(response.message())
        }
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        // Checking for connection errors
        onError(t.localizedMessage)
    }

    abstract fun onSuccess(result: T)

    abstract fun onError(message: String)
}