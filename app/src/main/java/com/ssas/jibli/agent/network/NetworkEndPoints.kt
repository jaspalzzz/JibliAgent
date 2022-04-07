package com.ssas.jibli.agent.network

import com.google.gson.JsonObject
import com.ssas.jibli.agent.BuildConfig
import com.ssas.jibli.agent.MApplication

object NetworkEndPoints {
	
	/* user */
	const val CREATE_USER_PROFILE = "users/createUserProfile"
	const val MOBILE_LOGIN_REQUEST = "users/mobileLoginRequest"
	const val SEARCH_CUSTOMER_ORDER = "order/searchCustomerOrder"
	const val SEARCH_CUSTOMER_ORDER_FOR_WATER="order/searchCustomerOrderForWater"
	const val SEARCH_CUSTOMER_ORDER_FOR_GAS="order/searchCustomerOrderForGas"
	const val SEARCH_CUSTOMER_ORDER_FOR_FOOD="order/searchCustomerOrderForFood"
	const val SEARCH_MERCHANT_STORES="merchant/searchMerchantStoreForAgents"
	const val SEARCH_MERCHANT_FOOD_STORES="merchant/searchMerchantFoodStoreForAgents"
	const val SEARCH_MERCHANT_WATER_STORES="merchant/searchMerchantWaterStoreForAgents"
	const val SEARCH_MERCHANT_GAS_STORES="merchant/searchMerchantGasStoreForAgents"
	const val STORE_PAYMENT_CHANNELS = "merchant/getStorePaymentChannels"
	const val SEARCH_NOTIFICATION_HISTORY = "push/searchNotificationHistory"
	const val RETRIEVE_USER_PASSWORD = "users/retrieveUserPassword"
	const val CHANGE_ORDER_STATUS = "order/changeOrderStatus"
	const val ORDER_CONFIRMATION_CODE = "order/generateOrderConfirmationCode"
	const val RESEND_ORDER_CONFIRMATION_CODE = "order/resendOrderConfirmationCode"
	const val DECLINE_PICKUP_ORDER = "order/declinePickupOrderByAgent"
	const val UPDATE_CUSTOMER_PAYMENT_ORDER_STATUS = "order/updateCustomerOrderPaymentStatus"


	/*
	* Auth json object method
	* */
	fun authJsonObject(): JsonObject {
		var params = JsonObject()
		params.addProperty("accessUserName", BuildConfig.ACCESS_USER_NAME)
		params.addProperty("accessPassword", BuildConfig.ACCESS_PASSWORD)
		params.addProperty("accessSmartSecurityKey", BuildConfig.SECURITY_KEY)
		params.addProperty("requestChannel", BuildConfig.REQUEST_CHANNEL)
		params.addProperty("languageCode", MApplication.language)
		return params
	}
}