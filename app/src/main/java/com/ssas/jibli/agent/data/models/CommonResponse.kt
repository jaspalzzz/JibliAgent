package com.ssas.jibli.agent.data.models

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("responseCodeVO") val responseCodeVO: ResponseCodeVO
)