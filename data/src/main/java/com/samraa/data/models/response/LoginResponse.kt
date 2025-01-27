package com.samraa.data.models.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("accessToken")
    val accessToken: String
) : BaseResponse()
