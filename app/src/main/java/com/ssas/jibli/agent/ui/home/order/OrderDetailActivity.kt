package com.ssas.jibli.agent.ui.home.order

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.constants.ValConstant
import com.ssas.jibli.agent.data.models.searchOrder.CustomerOrderDetailsList
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.databinding.ActivityOrderDetailBinding
import com.ssas.jibli.agent.dialogs.ConfirmOrderDialog
import com.ssas.jibli.agent.dialogs.ToastDialog
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeClickEvents
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.home.adapter.OrderProductAdapter
import com.ssas.jibli.agent.utils.Utils
import com.ssas.jibli.agent.widgets.PinView

class OrderDetailActivity : BaseActivity<ActivityOrderDetailBinding, HomeVM>() {

    private var orderOTPCode = ""
    private var orderTransactionId = ""
    private var productListAdapter: OrderProductAdapter? = null
    private var orderItem: OrderTransactionArr? = null
    private var isVerifiedOrder = false
    private var selectedTab = 0

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_order_detail, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        handleIntent(intent)
        initListener()
    }

    private fun initListener() {
        binding.pinview.showCodeText()
        binding.pinview.requestFocusFirstEt()
        binding.pinview.setPinViewListener(object : PinView.PinViewListener {
            override fun onPinCodeRecieved(pinCode: String) {
                orderOTPCode = pinCode
            }

            override fun onPinCodeDelete() {

            }
        })
    }

    private fun setToolbarTitle(title: Int) {
        binding.orderDetailToolbar.toolbarTitle.setText(title)
    }

    private fun initToolbar() {
        setToolbarTitle(R.string.review_order)
        binding.orderDetailToolbar.toolbarBackBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun handleIntent(intent: Intent?) {
        if (intent != null) {
            orderTransactionId = intent.getStringExtra(SharingKeys.ORDER_TRANSACTION_ID) ?: ""
            selectedTab =  intent.getIntExtra(SharingKeys.ORDER_API_TYPE,0)
            searchOrderDetails()
        }
    }

    private fun searchOrderDetails() {
        viewModel.searchCustomerOrders(0, 10, false, true, orderTransactionId, "",selectedTab)
    }

    override fun subscribeToEvents(vm: HomeVM) {
        binding.vm = vm

        vm.networkError.observe(this, Observer {
            if (it) {
                alertDialogShow(
                    this,
                    getString(R.string.no_network_title),
                    getString(R.string.no_network_connection)
                )
            }
        })

        vm.clickEvents.observe(this, Observer {
            when (it) {
                HomeClickEvents.SEND_ORDER_OTP -> {
                    vm.orderConfirmationCode(
                        orderItem?.orderTransactionId ?: "",
                        orderItem?.customerCode ?: ""
                    )
                }

                HomeClickEvents.RESEND_ORDER_OTP -> {
                    vm.resendOrderConfirmationCode(
                        orderItem?.orderTransactionId ?: "",
                        orderItem?.customerCode ?: ""
                    )
                }

                HomeClickEvents.CANCEL_ORDER_OTP -> {
                    isVerifiedOrder = false
                    binding.orderOTPView.visibility = View.GONE
                    binding.sendRequestBt.visibility = View.VISIBLE
                    binding.pinview.clearText()
                }

                HomeClickEvents.PICKUP_FROM_STORE -> {
                    vm.changeOrderStatus(
                        orderItem,
                        ValConstant.SHIPPED,
                        isVerifiedOrder,
                        orderOTPCode
                    )
                }
                HomeClickEvents.DELIVER_TO_CUSTOMER -> {
                    if (isVerifiedOrder) {
                        if (orderOTPCode.length < 4) {
                            alertDialogShow(this, getString(R.string.order_otp_error))
                        } else {
                            confirmOrderDialog()
                        }
                    } else {
                        confirmOrderDialog()
                    }
                }
                HomeClickEvents.CANCEL_PICKUP -> {
                    confirmPickupDeclineOrder()
                }
                HomeClickEvents.MAKE_CALL_CLICK -> {
                    Utils.callNumber(
                        this,
                        orderItem?.contactMobileNumberCode + orderItem?.contactMobileNumber
                    )
                }
                HomeClickEvents.VIEW_MAP -> {
                    if (orderItem?.customerAddressLatitude.isNullOrEmpty() || orderItem?.customerAddressLongitude.isNullOrEmpty()) {
                        alertDialogShow(this, "Location is not available")
                    } else {
                        var bundle = Bundle().apply {
                            putString(SharingKeys.CUSTOMER_LAT, orderItem?.customerAddressLatitude)
                            putString(SharingKeys.CUSTOMER_LON, orderItem?.customerAddressLongitude)
                            putString(
                                SharingKeys.DELIVERY_ADDRESS,
                                orderItem?.customerDeliveryAddress
                            )
                        }
                        Utils.jumpActivityWithData(this, ViewMapActivity::class.java, bundle)
                    }
                }
            }
        })

        vm.searchCustomerOrdersResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.SUCCESS -> {
                    dismissProgress()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == ApiStatusCodes.SEARCH_CUSTOMER_ORDER_SUCCESS &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        val response = it.response?.orderTransactionArr
                        if (!response.isNullOrEmpty()) {
                            orderItem = response?.get(0)
                            binding.item = orderItem
                            handleOrderStatus()
                            handlePaymentStatus()
                            handleDeliveryType()
                            paymentType(response?.get(0))
                            if (!response[0].customerOrderDetailsList.isNullOrEmpty()) {
                                inflateProductList(response.get(0).customerOrderDetailsList!!)
                            }
                        } else if (responseCode == "GE10010") {
                            alertDialogShow(this, getString(R.string.agent_barcode_error),
                                DialogInterface.OnClickListener { dialog, which ->
                                    finish()
                                }
                            )
                        } else {
                            alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                        }
                    } else {
                        alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                    }
                }

                Status.ERROR -> {
                    dismissProgress()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })

        vm.changeOrderStatusResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()

                }
                Status.SUCCESS -> {
                    dismissProgress()
                    when (it?.response?.responseCodeVO?.responseCode) {
                        "ORDERS10004" -> {
                            var dialog = ToastDialog.newInstance(R.drawable.ic_check)
                            dialog.show(supportFragmentManager, "")
                            Handler(Looper.myLooper()!!).postDelayed({
                                dialog.dismiss()
                                searchOrderDetails()
                                setResult(RequestCodes.CHANGE_ORDER_RESULT_CODE)
                            }, 1000)
                        }
                        "ORDERS10005" -> {
                                var dialog = ToastDialog.newInstance(R.drawable.ic_check)
                                dialog.show(supportFragmentManager, "")
                                Handler(Looper.myLooper()!!).postDelayed({
                                    dialog.dismiss()
                                    setResult(RequestCodes.CHANGE_ORDER_RESULT_CODE)
                                    finish()
                                }, 1000)
                        }
                        else -> {
                            binding.pinview?.clearText()
                            alertDialogShow(this, it?.response?.responseCodeVO?.responseValue ?: "")
                        }
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    binding.pinview?.clearText()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })

        vm.orderConfirmationCodeResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.SUCCESS -> {
                    dismissProgress()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == "OTPS10003" &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        showToast(this, it?.response?.responseCodeVO?.responseValue ?: "")
                        isVerifiedOrder = true
                        binding.orderOTPView.visibility = View.VISIBLE
                        binding.sendRequestBt.visibility = View.GONE
                    } else {
                        binding.pinview?.clearText()
                        alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                    }
                }

                Status.ERROR -> {
                    dismissProgress()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })

        vm.resendOrderConfirmationCodeResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.SUCCESS -> {
                    dismissProgress()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == "OTPS10003" &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        showToast(this, it?.response?.responseCodeVO?.responseValue ?: "")
                    } else {
                        alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                    }
                }

                Status.ERROR -> {
                    dismissProgress()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })

        vm.declinePickupOrderResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.SUCCESS -> {
                    dismissProgress()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage
                    if (responseCode == "ORDERS10013") {
                        showToast(this, it?.response?.responseCodeVO?.responseValue ?: "")
                        setResult(RequestCodes.CHANGE_ORDER_RESULT_CODE)
                        finish()
                    } else {
                        alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                    }
                }

                Status.ERROR -> {
                    dismissProgress()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })
    }

    private fun confirmOrderDialog() {
        var dialog = ConfirmOrderDialog.newInstance(
            orderItem?.orderTransactionId ?: "", R.drawable.ic_confirm_order,
            getString(R.string.confirm_delivery), getString(R.string.like_to_confirm_Delivery)
        )
        dialog.setListener(object : ConfirmOrderDialog.SuccessDialogListener {
            override fun onPositiveButtonClick(dialog: ConfirmOrderDialog) {
                dialog.dismiss()
                viewModel.changeOrderStatus(
                    orderItem,
                    ValConstant.DELIVERED,
                    isVerifiedOrder,
                    orderOTPCode
                )
            }

            override fun onCancelButtonClick(dialog: ConfirmOrderDialog) {
                dialog.dismiss()
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    private fun confirmPickupDeclineOrder() {
        var dialog = ConfirmOrderDialog.newInstance(
            orderItem?.orderTransactionId ?: "", R.drawable.ic_decline_pickup,
            getString(R.string.decline_pickup), getString(R.string.decline_pickup_message)
        )
        dialog.setListener(object : ConfirmOrderDialog.SuccessDialogListener {
            override fun onPositiveButtonClick(dialog: ConfirmOrderDialog) {
                dialog.dismiss()
                viewModel.declinePickupOrder(orderItem?.orderTransactionId ?: "")
            }

            override fun onCancelButtonClick(dialog: ConfirmOrderDialog) {
                dialog.dismiss()
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    private fun handlePaymentStatus() {
        binding.isPaid = orderItem?.paymentStatusCode == ValConstant.PAID
    }

    private fun handleOrderStatus() {
        when (orderItem?.statusCode) {

            ValConstant.ACCEPTED -> {
                binding.orderStatusText.text = getString(R.string.accepted)
            }

            ValConstant.READY_FOR_DELIVERY -> {
                binding.orderStatusText.text = getString(R.string.ready_for_pickup)
                binding.isAccepted = true
                binding.isShipped = false
            }
            ValConstant.SHIPPED -> {
                binding.orderStatusText.text = getString(R.string.under_delivery)
                binding.isShipped = true
                binding.isAccepted = false
            }
            ValConstant.DELIVERED -> {
                binding.orderStatusText.text = getString(R.string.delivered)
            }
        }
    }

    private fun paymentType(item: OrderTransactionArr) {
        if (item.isDinarPayDebitCard == "Y") {
            binding.paymentTypeText.text = getString(R.string.dinarpay)
        }
        if (item.isDinarPayVirtualCard == "Y") {
            binding.paymentTypeText.text = getString(R.string.dinarpay)
        }
        if (item.isSadadPay == "Y") {
            binding.paymentTypeText.text = getString(R.string.sadad_pay)
        }
        if (item.isMumalatPay == "Y") {
            binding.paymentTypeText.text = getString(R.string.muamalat_order)
        }
        if (item.isCashOnDelivery == "Y") {
            binding.paymentTypeText.text = getString(R.string.cash_order)
        }
    }

    private fun handleDeliveryType() {
        if (orderItem?.isRegularDelivery == "Y") {
            binding.deliveryTypeText.text = getString(R.string.regular)
            binding.deliveryTypeText.setTextColor(ActivityCompat.getColor(this, R.color.colorGreen))
        }
        if (orderItem?.isPremiumDelivery == "Y") {
            binding.deliveryTypeText.text = getString(R.string.premium)
            binding.deliveryTypeText.setTextColor(ActivityCompat.getColor(this, R.color.colorRed))
        }
    }

    private fun inflateProductList(customerOrderDetailsList: ArrayList<CustomerOrderDetailsList>) {
        productListAdapter = OrderProductAdapter(this)
        productListAdapter?.addData(customerOrderDetailsList)
        binding.productList.adapter = productListAdapter
    }
}