package com.ssas.jibli.agent.data.models.searchOrder

import com.google.gson.annotations.SerializedName
import com.ssas.jibli.agent.data.models.ResponseCodeVO

data class SearchOrderResponse(
    @SerializedName("responseCodeVO") val responseCodeVO: ResponseCodeVO,
    @SerializedName("orderTransactionArr") val orderTransactionArr: ArrayList<OrderTransactionArr>?
)