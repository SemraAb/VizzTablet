package com.samraa.data.models.request

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("deviceToken")
    var deviceToken: String
)
