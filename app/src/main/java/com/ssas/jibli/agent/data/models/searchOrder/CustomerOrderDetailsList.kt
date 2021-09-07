package com.ssas.jibli.agent.data.models.searchOrder

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerOrderDetailsList(
	@SerializedName("additionalRemarks") val additionalRemarks: String,
	@SerializedName("brandName") val brandName: String,
	@SerializedName("catalogueCode") val catalogueCode: String,
	@SerializedName("currencyCode") val currencyCode: String,
	@SerializedName("customerOrderDtlId") val customerOrderDtlId: String,
	@SerializedName("customerOrderMstId") val customerOrderMstId: String,
	@SerializedName("deliveryChargesByCatalogueCode") val deliveryChargesByCatalogueCode: String,
	@SerializedName("discountForTheProduct") val discountForTheProduct: String,
	@SerializedName("dtlShopingCartId") val dtlShopingCartId: String,
	@SerializedName("isCashOnDeliveryIsEligible") val isCashOnDeliveryIsEligible: String,
	@SerializedName("isDeleteChildOrder") val isDeleteChildOrder: String,
	@SerializedName("itemSequence") val itemSequence: String,
	@SerializedName("masterCategoryCode") val masterCategoryCode: String,
	@SerializedName("maximumRetailPrice") val maximumRetailPrice: String,
	@SerializedName("merchantVoucherCode") val merchantVoucherCode: String,
	@SerializedName("orderDetails") val orderDetails: String,
	@SerializedName("orderTransactionId") val orderTransactionId: String,
	@SerializedName("otherCharges1") val otherCharges1: String,
	@SerializedName("otherCharges2") val otherCharges2: String,
	@SerializedName("payableAmountForTheProduct") val payableAmountForTheProduct: String,
	@SerializedName("productDescription") val productDescription: String,
	@SerializedName("productImage1") val productImage1: String,
	@SerializedName("productName") val productName: String,
	@SerializedName("shelfCode") val shelfCode: String,
	@SerializedName("statusCode") val statusCode: String,
	@SerializedName("subCategoryCode") val subCategoryCode: String,
	@SerializedName("taxForTheProduct") val taxForTheProduct: String,
	@SerializedName("totalMaximumRetailPrice") val totalMaximumRetailPrice: String,
	@SerializedName("totalQuantity") val totalQuantity: String,
	@SerializedName("unitOfProduct") val unitOfProduct: String
) : Parcelable