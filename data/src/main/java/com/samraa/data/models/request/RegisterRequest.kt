package com.samraa.data.models.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("username")
    var username: String,
    @SerializedName("password")
    var password: String,
    @SerializedName("location")
    var location: String,
    @SerializedName("displayName")
    var displayName: String,
    @SerializedName("deviceToken")
    var deviceToken: String,

    )