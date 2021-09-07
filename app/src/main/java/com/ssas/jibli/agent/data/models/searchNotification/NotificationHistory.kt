package com.ssas.jibli.agent.data.models.searchNotification


import com.google.gson.annotations.SerializedName

data class NotificationHistory(
    @SerializedName("agentCode")
    val agentCode: String?= "",
    @SerializedName("customerCode")
    val customerCode: String?= "",
    @SerializedName("employeeCode")
    val employeeCode: String?= "",
    @SerializedName("isGeneral")
    val isGeneral: String?= "",
    @SerializedName("isMiscRequest1")
    val isMiscRequest1: String?= "",
    @SerializedName("isMiscRequest2")
    val isMiscRequest2: String?= "",
    @SerializedName("isOrderAccepted")
    val isOrderAccepted: String?= "",
    @SerializedName("isOrderCancelled")
    val isOrderCancelled: String?= "",
    @SerializedName("isOrderCreated")
    val isOrderCreated: String?= "",
    @SerializedName("isOrderDelivered")
    val isOrderDelivered: String?= "",
    @SerializedName("isOrderRejected")
    val isOrderRejected: String?= "",
    @SerializedName("isOrderShipped")
    val isOrderShipped: String?= "",
    @SerializedName("isOtherRequest")
    val isOtherRequest: String?= "",
    @SerializedName("merchantQRCode")
    val merchantQRCode: String? = "",
    @SerializedName("mobileCountryCode")
    val mobileCountryCode: String? = "",
    @SerializedName("mobileNumber")
    val mobileNumber: String? = "",
    @SerializedName("notificationDate")
    val notificationDate: String?= "",
    @SerializedName("notificationId")
    val notificationId: String? = "",
    @SerializedName("notificationMessage")
    val notificationMessage: String?= "",
    @SerializedName("transactionId")
    val transactionId: String?= ""
)