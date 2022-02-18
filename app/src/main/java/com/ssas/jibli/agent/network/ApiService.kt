package com.ssas.jibli.agent.network

import com.google.gson.JsonObject
import com.ssas.jibli.agent.data.models.CommonResponse
import com.ssas.jibli.agent.data.models.LoginResponse
import com.ssas.jibli.agent.data.models.merchantStore.MerchantStoresResponse
import com.ssas.jibli.agent.data.models.paymentChannel.PaymentChannelsResponse
import com.ssas.jibli.agent.data.models.searchOrder.SearchOrderResponse
import com.ssas.jibli.agent.data.models.searchNotification.SearchNotificationHistoryResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
	
	/***
	 * Network calls
	 */
	@POST(NetworkEndPoints.MOBILE_LOGIN_REQUEST)
	suspend fun loginRequest(@Body params: JsonObject): LoginResponse

	@POST(NetworkEndPoints.SEARCH_CUSTOMER_ORDER)
	suspend fun searchCustomerOrder(@Body params: JsonObject): SearchOrderResponse

	@POST(NetworkEndPoints.SEARCH_CUSTOMER_ORDER_FOR_WATER)
	suspend fun searchCustomerOrderForWater(@Body params: JsonObject): SearchOrderResponse

	@POST(NetworkEndPoints.SEARCH_CUSTOMER_ORDER_FOR_GAS)
	suspend fun searchCustomerOrderForGas(@Body params: JsonObject): SearchOrderResponse

	@POST(NetworkEndPoints.SEARCH_CUSTOMER_ORDER_FOR_FOOD)
	suspend fun searchOrderForFood(@Body params: JsonObject): SearchOrderResponse

	@POST(NetworkEndPoints.SEARCH_MERCHANT_STORES)
	suspend fun searchMerchantStores(@Body params: JsonObject): MerchantStoresResponse

	@POST(NetworkEndPoints.STORE_PAYMENT_CHANNELS)
	suspend fun getStorePaymentChannels(@Body params: JsonObject): PaymentChannelsResponse

	@POST(NetworkEndPoints.SEARCH_NOTIFICATION_HISTORY)
	suspend fun searchNotificationHistory(@Body params: JsonObject): SearchNotificationHistoryResponse

	@POST(NetworkEndPoints.RETRIEVE_USER_PASSWORD)
	suspend fun retrieveUserPassword(@Body params: JsonObject): CommonResponse

	@POST(NetworkEndPoints.CHANGE_ORDER_STATUS)
	suspend fun changeOrderStatus(@Body params: JsonObject): CommonResponse

	@POST(NetworkEndPoints.ORDER_CONFIRMATION_CODE)
	suspend fun orderConfirmationCode(@Body params: JsonObject): CommonResponse

	@POST(NetworkEndPoints.RESEND_ORDER_CONFIRMATION_CODE)
	suspend fun resendOrderConfirmationCode(@Body params: JsonObject): CommonResponse

	@POST(NetworkEndPoints.DECLINE_PICKUP_ORDER)
	suspend fun declinePickupOrder(@Body params: JsonObject): CommonResponse

	@POST(NetworkEndPoints.UPDATE_CUSTOMER_PAYMENT_ORDER_STATUS)
	suspend fun updateCustomerOrderPaymentStatus(@Body params: JsonObject): CommonResponse
}
