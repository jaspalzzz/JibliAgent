package com.ssas.jibli.agent.data.models.merchantStore


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SearchMerchantStoreArr(
    @SerializedName("createdDate")
    val createdDate: String? = "",
    @SerializedName("currencyCode")
    val currencyCode: String? = "",
    @SerializedName("merchantCategory")
    val merchantCategory: String? = "",
    @SerializedName("merchantCode")
    val merchantCode: String? = "",
    @SerializedName("merchantQRCode")
    val merchantQRCode: String? = "",
    @SerializedName("merchantRating")
    val merchantRating: String? = "",
    @SerializedName("merchantStoreId")
    val merchantStoreId: String? = "",
    @SerializedName("mstLatitude")
    val mstLatitude: String? = "",
    @SerializedName("mstLongitude")
    val mstLongitude: String? = "",
    @SerializedName("placesDescription")
    val placesDescription: String? = "",
    @SerializedName("placesDescriptionAR")
    val placesDescriptionAR: String? = "",
    @SerializedName("placesDescriptionFR")
    val placesDescriptionFR: String? = "",
    @SerializedName("statusCode")
    val statusCode: String? = "",
    @SerializedName("storeAddress")
    val storeAddress: String? = "",
    @SerializedName("storeCode")
    val storeCode: String? = "",
    @SerializedName("storeContactEmail")
    val storeContactEmail: String? = "",
    @SerializedName("storeContactNumber")
    val storeContactNumber: String? = "",
    @SerializedName("storeIconMainURL")
    val storeIconMainURL: String? = "",
    @SerializedName("storeName")
    val storeName: String? = "",
    @SerializedName("telephoneNumber")
    val telephoneNumber: String? = "",
    @SerializedName("websiteURL")
    val websiteURL: String? = ""
) : Parcelable