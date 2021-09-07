package com.ssas.jibli.agent.data.models.merchantStore


import com.google.gson.annotations.SerializedName
import com.ssas.jibli.agent.data.models.ResponseCodeVO

data class MerchantStoresResponse(
    @SerializedName("responseCodeVO")
    val responseCodeVO: ResponseCodeVO?,
    @SerializedName("searchMerchantStoreArr")
    val searchMerchantStoreArr: ArrayList<SearchMerchantStoreArr>?
)