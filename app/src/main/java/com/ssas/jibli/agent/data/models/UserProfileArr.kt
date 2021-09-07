package com.ssas.jibli.agent.data.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserProfileArr(
	@SerializedName("additionalRemarks") val additionalRemarks: String,
	@SerializedName("agentCode") val agentCode: String,
	@SerializedName("agreedTermsOfUsage") val agreedTermsOfUsage: String,
	@SerializedName("countryCode") val countryCode: String,
	@SerializedName("countryId") val countryId: String,
	@SerializedName("customerCode") val customerCode: String,
	@SerializedName("emailAddress") val emailAddress: String,
	@SerializedName("firstName") val firstName: String,
	@SerializedName("gender") val gender: String,
	@SerializedName("isAgent") val isAgent: String,
	@SerializedName("isCustomer") val isCustomer: String,
	@SerializedName("lastName") val lastName: String,
	@SerializedName("loginUserName") val loginUserName: String,
	@SerializedName("merchantCode") val merchantCode: String,
	@SerializedName("mobileNumber") val mobileNumber: String,
	@SerializedName("mobileNumberCountryCode") val mobileNumberCountryCode: String,
	@SerializedName("profileImage1") val profileImage1: String,
	@SerializedName("profileImage2") val profileImage2: String,
	@SerializedName("statusCode") val statusCode: String,
	@SerializedName("transactionId") val transactionId: String,
	@SerializedName("userProfileId") val userProfileId: String,
	@SerializedName("eKycStatus") val eKycStatus: String
) : Parcelable