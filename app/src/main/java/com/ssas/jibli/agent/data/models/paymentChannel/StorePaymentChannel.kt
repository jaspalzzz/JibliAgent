package com.ssas.jibli.agent.data.models.paymentChannel


import com.google.gson.annotations.SerializedName

data class StorePaymentChannel(
    @SerializedName("channelCode")
    val channelCode: String?,
    @SerializedName("channelDescriptionAR")
    val channelDescriptionAR: String?,
    @SerializedName("channelDescriptionEN")
    val channelDescriptionEN: String?,
    @SerializedName("currencyCode")
    val currencyCode: String?,
    @SerializedName("imageURL")
    val imageURL: String?,
    @SerializedName("isEnabled")
    val isEnabled: String?,
    @SerializedName("statusCode")
    val statusCode: String?,
    @SerializedName("storeCode")
    val storeCode: String?
)