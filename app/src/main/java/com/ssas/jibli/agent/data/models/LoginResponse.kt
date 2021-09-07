package com.ssas.jibli.agent.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("responseCodeVO") val responseCodeVO: ResponseCodeVO,
    @SerializedName("userProfileArr") val userProfileArr: ArrayList<UserProfileArr>
)