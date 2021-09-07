package com.ssas.jibli.agent.data.models.searchOrder


import com.google.gson.annotations.SerializedName

data class OrderTransactionArr(
	@SerializedName("additionalRemarks")
	val additionalRemarks: String?,
	@SerializedName("addressName")
	val addressName: String?,
	@SerializedName("cartTransactionId")
	val cartTransactionId: String?,
	@SerializedName("cityName")
	val cityName: String?,
	@SerializedName("contactMobileNumber")
	val contactMobileNumber: String?,
	@SerializedName("contactMobileNumberCode")
	val contactMobileNumberCode: String?,
	@SerializedName("contactPersonName")
	val contactPersonName: String?,
	@SerializedName("currencyCode")
	val currencyCode: String?,
	@SerializedName("customerAddressLatitude")
	val customerAddressLatitude: String?,
	@SerializedName("customerAddressLongitude")
	val customerAddressLongitude: String?,
	@SerializedName("customerCode")
	val customerCode: String?,
	@SerializedName("customerFullName")
	val customerFullName: String?,
	@SerializedName("customerLatitude")
	val customerLatitude: String?,
	@SerializedName("customerLongitude")
	val customerLongitude: String?,
	@SerializedName("customerOrderDetailsList")
	val customerOrderDetailsList: ArrayList<CustomerOrderDetailsList>?,
	@SerializedName("customerOrderMstId")
	val customerOrderMstId: String?,
	@SerializedName("deliveryAddress")
	val deliveryAddress: String?,
	@SerializedName("deliveryAddressCode")
	val deliveryAddressCode: String?,
	@SerializedName("deliveryAddressLine1")
	val deliveryAddressLine1: String?,
	@SerializedName("deliveryAddressLine2")
	val deliveryAddressLine2: String?,
	@SerializedName("deliveryAgentCode")
	val deliveryAgentCode: String?,
	@SerializedName("deliveryLocationId")
	val deliveryLocationId: String?,
	@SerializedName("deliveryTimingsI")
	val deliveryTimingsI: String?,
	@SerializedName("dinarPayCustomerAccount")
	val dinarPayCustomerAccount: String?,
	@SerializedName("emailAddress")
	val emailAddress: String?,
	@SerializedName("finalPaymentAmount")
	val finalPaymentAmount: String?,
	@SerializedName("isCashOnDelivery")
	val isCashOnDelivery: String?,
	@SerializedName("isDinarPayDebitCard")
	val isDinarPayDebitCard: String?,
	@SerializedName("isDinarPayVirtualCard")
	val isDinarPayVirtualCard: String?,
	@SerializedName("isIdfaliPay")
	val isIdfaliPay: String?,
	@SerializedName("isMizaPay")
	val isMizaPay: String?,
	@SerializedName("isMobinapPay")
	val isMobinapPay: String?,
	@SerializedName("isMumalatPay")
	val isMumalatPay: String?,
	@SerializedName("isSadadPay")
	val isSadadPay: String?,
	@SerializedName("isTadawelPay")
	val isTadawelPay: String?,
	@SerializedName("merchantQRCode")
	val merchantQRCode: String?,
	@SerializedName("mstShopingCartId")
	val mstShopingCartId: String?,
	@SerializedName("orderDate")
	val orderDate: String?,
	@SerializedName("orderHistoryList")
	val orderHistoryList: ArrayList<OrderHistoryList>?,
	@SerializedName("orderTransactionId")
	val orderTransactionId: String?,
	@SerializedName("paymentDate")
	val paymentDate: String?,
	@SerializedName("paymentStatusId")
	val paymentStatusId: String?,
	@SerializedName("paymentTransactionId")
	val paymentTransactionId: String?,
	@SerializedName("paymentType")
	val paymentType: String?,
	@SerializedName("placesCode")
	val placesCode: String?,
	@SerializedName("pobox")
	val pobox: String?,
	@SerializedName("refundDate")
	val refundDate: String?,
	@SerializedName("refundTransactionId")
	val refundTransactionId: String?,
	@SerializedName("reviewRating")
	val reviewRating: String?,
	@SerializedName("specialInstructions")
	val specialInstructions: String?,
	@SerializedName("statusCode")
	var statusCode: String?,
	@SerializedName("storeAddress")
	val storeAddress: String?,
	@SerializedName("storeCode")
	val storeCode: String?,
	@SerializedName("storeContactEmail")
	val storeContactEmail: String?,
	@SerializedName("storeContactNumber")
	val storeContactNumber: String?,
	@SerializedName("storeIconAltURL")
	val storeIconAltURL: String?,
	@SerializedName("storeIconMain")
	val storeIconMain: String?,
	@SerializedName("storeIconMainURL")
	val storeIconMainURL: String?,
	@SerializedName("storeName")
	val storeName: String?,
	@SerializedName("totalDeliveryCharges")
	val totalDeliveryCharges: String?,
	@SerializedName("totalDiscountAmount")
	val totalDiscountAmount: String?,
	@SerializedName("totalOrderAmount")
	val totalOrderAmount: String?,
	@SerializedName("totalQuantityInCart")
	val totalQuantityInCart: String?,
	@SerializedName("totalTaxAmount")
	val totalTaxAmount: String?,
	@SerializedName("userProfileId")
	val userProfileId: String?,
	@SerializedName("customerDeliveryAddress")
	val customerDeliveryAddress: String?,
	@SerializedName("agentName")
	val agentName: String?,
	@SerializedName("agentPhoneNumber")
	val agentPhoneNumber: String?,
	@SerializedName("isRegularDelivery")
	val isRegularDelivery: String?,
	@SerializedName("isPremiumDelivery")
	val isPremiumDelivery: String?,
	var accessUserName:String?,
	var accessPassword:String?,
	var accessSmartSecurityKey:String?,
	var requestChannel:String?,
	var languageCode:String?,
	var isOrderConfirmationRequired:String?,
	var orderConfirmationCode:String?

)