package com.ssas.jibli.agent.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseCodeVO(
	@SerializedName("otherValues") val otherValues: String,
	@SerializedName("responseCode") val responseCode: String,
	@SerializedName("responseMessage") val responseMessage: String,
	@SerializedName("responseValue") val responseValue: String
) : Parcelable