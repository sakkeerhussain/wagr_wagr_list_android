package com.grabclone.driver.api

import com.google.gson.annotations.SerializedName
import com.grabclone.driver.*

open class BaseRespModel(val status: String, val message: String)
//open class BaseNotiModel(val type: String, val badge: Int)

// API response models
class LoginRespModel(val data: LoginResp): BaseRespModel("", "")

class LoginResp(val token: String, val user: User)

class DriverRespModel(val data: Driver): BaseRespModel("", "")
class DriversRespModel(val data: List<Driver>): BaseRespModel("", "")
class VehicleModelsRespModel(val data: List<VehicleModel>): BaseRespModel("", "")

class FcmTokenRespModel(val data: FcmToken): BaseRespModel("", "")
class FcmToken(@SerializedName("fcm_token") val fcmToken: String)
class RideReqRespModel(val data: RideRequest) : BaseRespModel("", "")
class RideRespModel(val data: Ride) : BaseRespModel("", "")

// Notifications response models
//class RideReqNotiModel(val payload: RideRequest): BaseNotiModel("", 0)
