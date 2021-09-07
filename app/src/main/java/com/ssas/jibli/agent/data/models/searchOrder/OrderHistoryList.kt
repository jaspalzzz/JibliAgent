package com.ssas.jibli.agent.data.models.searchOrder

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderHistoryList(
	@SerializedName("orderActionDate") val orderActionDate: String?="",
	@SerializedName("orderActionStatusCode") val orderActionStatusCode: String? = "",
	@SerializedName("orderTransactionId") val orderTransactionId: String?= ""
) : Parcelable