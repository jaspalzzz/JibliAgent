package com.ssas.jibli.agent.data.models.paymentChannel


import com.google.gson.annotations.SerializedName
import com.ssas.jibli.agent.data.models.ResponseCodeVO

data class PaymentChannelsResponse(
    @SerializedName("responseCodeVO")
    val responseCodeVO: ResponseCodeVO?,
    @SerializedName("storePaymentChannelList")
    val storePaymentChannelList: List<StorePaymentChannel>?
)