package com.ssas.jibli.agent.repo.home

import com.google.gson.JsonObject
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.data.models.CommonResponse
import com.ssas.jibli.agent.data.models.merchantStore.MerchantStoresResponse
import com.ssas.jibli.agent.data.models.paymentChannel.PaymentChannelsResponse
import com.ssas.jibli.agent.data.models.searchOrder.SearchOrderResponse
import com.ssas.jibli.agent.network.ApiService
import com.ssas.jibli.agent.data.models.searchNotification.SearchNotificationHistoryResponse
import javax.inject.Inject

class HomeRepo {

    @Inject
    lateinit var apiService: ApiService

    init {
        MApplication.netComponents.inject(this)
    }

    suspend fun searchCustomerOrder(params: JsonObject): SearchOrderResponse{
        return apiService.searchCustomerOrder(params)
    }

    suspend fun searchCustomerOrderForWater(params: JsonObject): SearchOrderResponse{
        return apiService.searchCustomerOrderForWater(params)
    }

    suspend fun searchCustomerOrderForGas(params: JsonObject): SearchOrderResponse{
        return apiService.searchCustomerOrderForGas(params)
    }

    suspend fun searchMerchantStores(params: JsonObject): MerchantStoresResponse{
        return apiService.searchMerchantStores(params)
    }

    suspend fun getStorePaymentChannels(params: JsonObject): PaymentChannelsResponse{
        return apiService.getStorePaymentChannels(params)
    }

    suspend fun searchNotificationHistory(params: JsonObject): SearchNotificationHistoryResponse{
        return apiService.searchNotificationHistory(params)
    }

    suspend fun changeOrderStatus(params: JsonObject): CommonResponse{
        return apiService.changeOrderStatus(params)
    }

    suspend fun orderConfirmationCode(params: JsonObject): CommonResponse{
        return apiService.orderConfirmationCode(params)
    }

    suspend fun resendOrderConfirmationCode(params: JsonObject): CommonResponse{
        return apiService.resendOrderConfirmationCode(params)
    }

    suspend fun declinePickupOrder(params: JsonObject): CommonResponse{
        return apiService.declinePickupOrder(params)
    }

    suspend fun updateCustomerOrderPaymentStatus(params: JsonObject): CommonResponse{
        return apiService.updateCustomerOrderPaymentStatus(params)
    }

}