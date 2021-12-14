package com.ssas.jibli.agent.repo.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.ssas.jibli.agent.BuildConfig
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.data.constants.ValConstant
import com.ssas.jibli.agent.data.models.CommonResponse
import com.ssas.jibli.agent.data.models.merchantStore.MerchantStoresResponse
import com.ssas.jibli.agent.data.models.paymentChannel.PaymentChannelsResponse
import com.ssas.jibli.agent.data.models.searchOrder.SearchOrderResponse
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.network.APIResponse
import com.ssas.jibli.agent.network.NetworkEndPoints
import com.ssas.jibli.agent.data.models.searchNotification.SearchNotificationHistoryResponse
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.lang.Exception
import javax.inject.Inject

class HomeVM(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var prefMain: PrefMain

    init {
        MApplication.appComponent.inject(this)
    }

    /*Repo*/
    var homeRepo = HomeRepo()

    var clickEvents = MutableLiveData<HomeClickEvents>()

    /*Network error*/
    var networkError = MutableLiveData<Boolean>()

    /*
    * Methods
    * */
    fun orderSeeAllClick() {
        clickEvents.value = HomeClickEvents.ORDER_SEE_ALL_CLICK
    }

    fun viewShopsClick() {
        clickEvents.value = HomeClickEvents.VIEW_SHOPS_CLICK
    }

    fun homeNotificationsClick() {
        clickEvents.value = HomeClickEvents.NOTIFICATIONS_CLICK
    }

    fun pickupFromStoreClick() {
        clickEvents.value = HomeClickEvents.PICKUP_FROM_STORE
    }

    fun cancelPickupClick() {
        clickEvents.value = HomeClickEvents.CANCEL_PICKUP
    }

    fun viewMapClick() {
        clickEvents.value = HomeClickEvents.VIEW_MAP
    }

    fun makeCallClick() {
        clickEvents.value = HomeClickEvents.MAKE_CALL_CLICK
    }

    fun deliverToCustomer() {
        clickEvents.value = HomeClickEvents.DELIVER_TO_CUSTOMER
    }

    fun sendOrderOTP() {
        clickEvents.value = HomeClickEvents.SEND_ORDER_OTP
    }

    fun candelOrderOTP() {
        clickEvents.value = HomeClickEvents.CANCEL_ORDER_OTP
    }

    fun resendOrderOTP() {
        clickEvents.value = HomeClickEvents.RESEND_ORDER_OTP
    }

    fun logoutClick() {
        clickEvents.value = HomeClickEvents.LOGOUT_BUTTON
    }

    fun myOrdersClick() {
        clickEvents.value = HomeClickEvents.MY_ORDERS
    }

    fun pickupDeliveriesClick() {
        clickEvents.value = HomeClickEvents.PICKUP_DELIVERIES
    }

    fun allFilterClick() {
        clickEvents.value = HomeClickEvents.FILTER_ALL_CLICK
    }

    fun readyForPickupFilterClick() {
        clickEvents.value = HomeClickEvents.FILTER_READY_FOR_PICKUP_CLICK
    }

    fun underDeliveryFilterClick() {
        clickEvents.value = HomeClickEvents.FILTER_UNDER_DELIVERY_CLICK
    }

    fun deliveredFilterClick() {
        clickEvents.value = HomeClickEvents.FILTER_DELIVERED_CLICK
    }

    fun onFilterButtonClick() {
        clickEvents.value = HomeClickEvents.FILTER_CLICK
    }

    /*
    * SearchOrderNotification
    * */
    val searchOrder = MutableLiveData<APIResponse<SearchOrderResponse>>()

    fun searchOrderDetails(
        pageNumber: Int,
        limit: Int,
        hasPagination: Boolean,
        viewChildOrders: Boolean,
        orderTransactionId: String,
        statusCode: String
    ) {
        if (Utils.isInternet(getApplication())) {
            searchOrder.postValue(APIResponse<SearchOrderResponse>().onLoading() as APIResponse<SearchOrderResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("deliveryAgentCode", prefMain[PrefKeys.SALES_AGENT_CODE, ""])
            params.addProperty("seeAllOrderDetails", "Y")
            if (viewChildOrders) {
                params.addProperty("viewChildOrderDetails", "Y")
                params.addProperty("orderTransactionId", orderTransactionId)
            }
            if (hasPagination) {
                params.addProperty("pageNumber", pageNumber)
                params.addProperty("limit", limit)
            }
            if (!statusCode.isNullOrEmpty()) {
                params.addProperty("statusCode", statusCode)
            }

            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.searchOrder(params)
                    searchOrder.postValue(APIResponse<SearchOrderResponse>().onSuccess(response) as APIResponse<SearchOrderResponse>?)
                } catch (e: Exception) {
                    searchOrder.postValue(APIResponse<SearchOrderResponse>().onError(e) as APIResponse<SearchOrderResponse>)
                }
            }
        } else {
            networkError.value = true
        }
    }

    /*
    * Search Merchant Stores
    * */
    val searchMerchantStoresResponse = MutableLiveData<APIResponse<MerchantStoresResponse>>()

    fun searchMerchantStores(pageNumber: Int, limit: Int) {
        if (Utils.isInternet(getApplication())) {
            searchMerchantStoresResponse.postValue(APIResponse<MerchantStoresResponse>().onLoading() as APIResponse<MerchantStoresResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("agentCode", prefMain[PrefKeys.SALES_AGENT_CODE, ""])
            params.addProperty("seeAllMerchantStores", "Y")
            params.addProperty("pageNumber", pageNumber)
            params.addProperty("limit", limit)
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.searchMerchantStores(params)
                    searchMerchantStoresResponse.postValue(
                        APIResponse<MerchantStoresResponse>().onSuccess(
                            response
                        ) as APIResponse<MerchantStoresResponse>?
                    )
                } catch (e: Exception) {
                    searchMerchantStoresResponse.postValue(
                        APIResponse<MerchantStoresResponse>().onError(
                            e
                        ) as APIResponse<MerchantStoresResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }

    /*
     * get payment channels
     * */
    val paymentChannelsResponse = MutableLiveData<APIResponse<PaymentChannelsResponse>>()
    fun getStorePaymentChannels(storeCode: String) {
        if (Utils.isInternet(getApplication())) {
            paymentChannelsResponse.postValue(APIResponse<PaymentChannelsResponse>().onLoading() as APIResponse<PaymentChannelsResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("storeCode", storeCode)
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.getStorePaymentChannels(params)
                    paymentChannelsResponse.postValue(
                        APIResponse<PaymentChannelsResponse>().onSuccess(
                            response
                        ) as APIResponse<PaymentChannelsResponse>
                    )
                } catch (e: Exception) {
                    paymentChannelsResponse.postValue(
                        APIResponse<PaymentChannelsResponse>().onError(
                            e
                        ) as APIResponse<PaymentChannelsResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }

    /*
        * search order notification history
        * */
    val searchNotificationHistoryResponse =
        MutableLiveData<APIResponse<SearchNotificationHistoryResponse>>()

    fun searchNotificationHistory(limit: Int, pageNumber: Int) {
        if (Utils.isInternet(getApplication())) {
            searchNotificationHistoryResponse.postValue(
                APIResponse<SearchNotificationHistoryResponse>().onLoading() as APIResponse<SearchNotificationHistoryResponse>
            )
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("agentCode", prefMain[PrefKeys.SALES_AGENT_CODE, ""])
            params.addProperty("isOrderRelatedNotifications", "Y")
            params.addProperty("limit", limit)
            params.addProperty("pageNumber", pageNumber)
            params.addProperty("seeAllRequestDetails", "Y")
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.searchNotificationHistory(params)
                    searchNotificationHistoryResponse.postValue(
                        APIResponse<SearchNotificationHistoryResponse>().onSuccess(
                            response
                        ) as APIResponse<SearchNotificationHistoryResponse>
                    )
                } catch (e: Exception) {
                    searchNotificationHistoryResponse.postValue(
                        APIResponse<SearchNotificationHistoryResponse>().onError(
                            e
                        ) as APIResponse<SearchNotificationHistoryResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }


    /*
     * search order notification history
     * */
    val changeOrderStatusResponse =
        MutableLiveData<APIResponse<CommonResponse>>()

    fun changeOrderStatus(
        orderItem: OrderTransactionArr?,
        statusCode: String,
        isVerifiedOrder: Boolean,
        orderOTPCode: String
    ) {
        if (Utils.isInternet(getApplication())) {
            changeOrderStatusResponse.postValue(
                APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>
            )
            orderItem?.accessUserName = BuildConfig.ACCESS_USER_NAME
            orderItem?.accessPassword = BuildConfig.ACCESS_PASSWORD
            orderItem?.accessSmartSecurityKey = BuildConfig.SECURITY_KEY
            orderItem?.requestChannel = BuildConfig.REQUEST_CHANNEL
            orderItem?.languageCode = MApplication.language
            orderItem?.statusCode = statusCode
            if (isVerifiedOrder) {
                orderItem?.isOrderConfirmationRequired = "Y"
                orderItem?.orderConfirmationCode = orderOTPCode
            }

            if (orderItem?.isCashOnDelivery == "Y" || orderItem?.isMumalatPay == "Y") {
                orderItem?.paymentStatusCode = ValConstant.PAID
            }

            var paramsString = Gson().toJson(orderItem)
            var params = Gson().fromJson(paramsString, JsonObject::class.java)

            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.changeOrderStatus(params)
                    changeOrderStatusResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>
                    )
                } catch (e: Exception) {
                    changeOrderStatusResponse.postValue(
                        APIResponse<CommonResponse>().onError(
                            e
                        ) as APIResponse<CommonResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }


    // update payment status code

    val updatePaymentStatusResponse =
        MutableLiveData<APIResponse<CommonResponse>>()

    fun updateCustomerOrderPaymentStatus(orderItem: OrderTransactionArr?) {
        if (Utils.isInternet(getApplication())) {
            updatePaymentStatusResponse.postValue(
                APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>
            )
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("customerCode", orderItem?.customerCode)
            params.addProperty("userProfileId", orderItem?.userProfileId)
            params.addProperty("orderTransactionId", orderItem?.orderTransactionId)
            params.addProperty("customerOrderMstId", orderItem?.customerOrderMstId)
            params.addProperty("paymentStatusCode", "PAID")
            params.addProperty("paymentTransactionId", orderItem?.paymentTransactionId)

            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.updateCustomerOrderPaymentStatus(params)
                    updatePaymentStatusResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>
                    )
                } catch (e: Exception) {
                    updatePaymentStatusResponse.postValue(
                        APIResponse<CommonResponse>().onError(
                            e
                        ) as APIResponse<CommonResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }


    /*
    * order confirmation code
    * */
    val orderConfirmationCodeResponse = MutableLiveData<APIResponse<CommonResponse>>()
    fun orderConfirmationCode(orderTransactionId: String, customerCode: String) {
        if (Utils.isInternet(getApplication())) {
            orderConfirmationCodeResponse.postValue(APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("orderTransactionId", orderTransactionId)
            params.addProperty("customerCode", customerCode)
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.orderConfirmationCode(params)
                    orderConfirmationCodeResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>
                    )
                } catch (e: Exception) {
                    orderConfirmationCodeResponse.postValue(
                        APIResponse<CommonResponse>().onError(
                            e
                        ) as APIResponse<CommonResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }


    /*
     * resend order confirmation code
     * */
    val resendOrderConfirmationCodeResponse = MutableLiveData<APIResponse<CommonResponse>>()
    fun resendOrderConfirmationCode(orderTransactionId: String, customerCode: String) {
        if (Utils.isInternet(getApplication())) {
            resendOrderConfirmationCodeResponse.postValue(APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("orderTransactionId", orderTransactionId)
            params.addProperty("customerCode", customerCode)
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.resendOrderConfirmationCode(params)
                    resendOrderConfirmationCodeResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>
                    )
                } catch (e: Exception) {
                    resendOrderConfirmationCodeResponse.postValue(
                        APIResponse<CommonResponse>().onError(
                            e
                        ) as APIResponse<CommonResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }

    /*
     * resend order confirmation code
     * */
    val declinePickupOrderResponse = MutableLiveData<APIResponse<CommonResponse>>()
    fun declinePickupOrder(orderTransactionId: String) {
        if (Utils.isInternet(getApplication())) {
            declinePickupOrderResponse.postValue(APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("orderTransactionId", orderTransactionId)
            params.addProperty("deliveryAgentCode", prefMain[PrefKeys.SALES_AGENT_CODE, ""])
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = homeRepo.declinePickupOrder(params)
                    declinePickupOrderResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>
                    )
                } catch (e: Exception) {
                    declinePickupOrderResponse.postValue(
                        APIResponse<CommonResponse>().onError(
                            e
                        ) as APIResponse<CommonResponse>
                    )
                }
            }
        } else {
            networkError.value = true
        }
    }
}