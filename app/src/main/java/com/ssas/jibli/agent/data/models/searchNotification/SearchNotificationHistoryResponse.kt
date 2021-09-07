package com.ssas.jibli.agent.data.models.searchNotification


import com.google.gson.annotations.SerializedName
import com.ssas.jibli.agent.data.models.ResponseCodeVO


data class SearchNotificationHistoryResponse(
    @SerializedName("notificationHistoryList")
    val notificationHistoryList: ArrayList<NotificationHistory>?,
    @SerializedName("responseCodeVO")
    val responseCodeVO: ResponseCodeVO?
)