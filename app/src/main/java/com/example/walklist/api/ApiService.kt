package com.grabclone.driver.api

import android.content.Context
import com.example.walklist.BuildConfig
import com.example.walklist.controllers.UserController
import com.example.walklist.utils.Const
import com.example.walklist.utils.User
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


object ApiService {

    interface RetrofitService {

        @POST("user/login/")
        fun login(@Body cred: LoginReqModel): Call<LoginRespModel>

        @POST("user/register/")
        fun register(@Body user: User): Call<LoginRespModel>

//        @GET("user/profile/")
//        fun profile(): Call<DriverRespModel>
//
//        @PUT("user/location/")
//        fun updateUserLocation(@Body body: GpsPosition): Call<DriverRespModel>
//
//        @GET("user/nearby_drivers/")
//        fun getNearbyDrivers(@Query("pos_lat") lat: Double,
//                             @Query("pos_long") long: Double,
//                             @Query("max_distance") distance: Float): Call<DriversRespModel>
//
//        @PUT("profile/fcm_token/")
//        fun updateFcmToken(@Body body: FcmToken): Call<FcmTokenRespModel>
//
//        @GET("driver/ride_request/{id}/")
//        fun getRideRequest(@Path("id") id: Int): Call<RideReqRespModel>
//
//        @GET("driver/ride_decline/{id}/")
//        fun declineRide(@Path("id") id: Int): Call<RideReqRespModel>
//
//        @GET("driver/ride_accept/{id}/")
//        fun acceptRide(@Path("id") id: Int): Call<RideRespModel>
//
//        @POST("driver/ride_update/{id}/")
//        fun updateRide(@Path("id") id: Int, @Body location: GpsPosition): Call<RideRespModel>
//
//        @GET("driver/ride_start/{id}/")
//        fun startRide(@Path("id") id: Int): Call<RideRespModel>
//
//        @GET("driver/ride_stop/{id}/")
//        fun stopRide(@Path("id") id: Int): Call<RideRespModel>
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

        val headerInterceptor = object: Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()

                val builder = request.newBuilder()
                        .addHeader("Content-Type", "application/json")
                if (mTokenInService != "") {
                    builder.addHeader("Authorization", "Token $mTokenInService")
                }

                return chain.proceed(builder.build())
            }
        }

        val httpClient = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(Const.BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        return retrofit.create<RetrofitService>(RetrofitService::class.java)
    }
}