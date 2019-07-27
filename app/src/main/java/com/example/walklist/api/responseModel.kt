package com.example.walklist.api

import com.example.walklist.utils.User
import com.example.walklist.utils.Walk

open class BaseRespModel(val status: String, val message: String)
//open class BaseNotiModel(val type: String, val badge: Int)

// API response models
class LoginRespModel(val data: LoginResp): BaseRespModel("", "")
class LoginResp(val token: String, val user: User)

class WalksRespModel(val data: List<Walk>): BaseRespModel("", "")
class WalkRespModel(val data: Walk): BaseRespModel("", "")

//class DriverRespModel(val data: Driver): BaseRespModel("", "")
//class VehicleModelsRespModel(val data: List<VehicleModel>): BaseRespModel("", "")
//
//class FcmTokenRespModel(val data: FcmToken): BaseRespModel("", "")
//class FcmToken(@SerializedName("fcm_token") val fcmToken: String)
//class RideReqRespModel(val data: RideRequest) : BaseRespModel("", "")
//class RideRespModel(val data: Ride) : BaseRespModel("", "")

// Notifications response models
//class RideReqNotiModel(val payload: RideRequest): BaseNotiModel("", 0)
