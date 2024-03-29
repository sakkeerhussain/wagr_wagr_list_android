package com.example.walklist.api

import android.content.Context
import com.example.walklist.BuildConfig
import com.example.walklist.controllers.UserController
import com.example.walklist.utils.Const
import com.example.walklist.utils.User
import com.example.walklist.utils.Walk
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object ApiService {

    interface RetrofitService {

        @POST("login")
        fun login(@Body cred: LoginReqModel): Call<LoginRespModel>

        @POST("register")
        fun register(@Body user: User): Call<LoginRespModel>

        @GET("walks")
        fun getWalks(): Call<WalksRespModel>

        @GET("walks/active")
        fun getActiveWalk(): Call<WalkRespModel>

        @POST("walks")
        fun createWalk(@Body body: Walk): Call<WalkRespModel>

        @POST("walks/{id}/end")
        fun endWalk(@Path("id") id: Int, @Body body: Walk): Call<WalkRespModel>

        @PUT("walks/{id}")
        fun updateWalk(@Path("id") id: Int, @Body body: Walk): Call<WalkRespModel>
    }

    private var mService: RetrofitService? = null
    private var mTokenInService: String? = null

    fun getService(context: Context): RetrofitService {
        mTokenInService = UserController.getToken(context)
        return mService ?: createApiService()
    }

    private fun createApiService(): RetrofitService {

        val loggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            loggingInterceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request()

            val builder = request.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
            if (mTokenInService != "") {
                builder.addHeader("Authorization", "Bearer $mTokenInService")
            }

            chain.proceed(builder.build())
        }

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build()

        val gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val retrofit = Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        /// 2019-07-27 06:06:30
        return retrofit.create(RetrofitService::class.java)
    }
}