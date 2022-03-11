package com.ssas.jibli.agent.ui.home.shops

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.UserProfileArr
import com.ssas.jibli.agent.data.models.merchantStore.SearchMerchantStoreArr
import com.ssas.jibli.agent.data.models.paymentChannel.StorePaymentChannel
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.databinding.ActivityViewMerchantBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeVM

class ViewMerchantActivity : BaseActivity<ActivityViewMerchantBinding, HomeVM>() {

    private var DINARPAY_CODE = "PGC002_DINARPAY_CARD_BUSINESS"
    private var SADADPAY_CODE = "PGC004_SADADPAY_CARD"
    private var CASH_DELIVERY_CODE = "PGC001_CASH_ON_DELIVERY"
    var merchantStore: SearchMerchantStoreArr? = null


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_view_merchant, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        agentDetails()
        handleIntent(intent)
    }

    private fun agentDetails() {
        var agentDetailString = prefMain[PrefKeys.AGENT_PROFILE_DETAILS, ""]
        if (agentDetailString?.isNotEmpty() == true) {
            var agentDetails =
                Gson().fromJson<UserProfileArr>(agentDetailString, UserProfileArr::class.java)
            binding?.agentProfileImageString = agentDetails?.profileImage1
            binding?.agentNameText.text = agentDetails?.firstName + " " + agentDetails?.lastName
            binding?.agentCodeText.text = agentDetails?.agentCode
        }
    }

    private fun handleIntent(intent: Intent?) {
        intent.let {
            merchantStore =
                it?.getParcelableExtra<SearchMerchantStoreArr>(SharingKeys.MERCHANT_STORE)
            if (merchantStore != null) {
                binding.item = merchantStore
                getPaymentChannels(merchantStore?.storeCode)
            }
        }
    }

    private fun getPaymentChannels(storeCode: String?) {
        viewModel.getStorePaymentChannels(storeCode ?: "")
    }


    private fun initToolbar() {
        binding.backPressImage.setOnClickListener {
            onBackPressed()
        }
    }

    override fun subscribeToEvents(vm: HomeVM) {

        vm.networkError.observe(this, Observer {
            if (it) {
                alertDialogShow(
                    this,
                    getString(R.string.no_network_title),
                    getString(R.string.no_network_connection)
                )
            }
        })

        vm.paymentChannelsResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                }

                Status.SUCCESS -> {
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == ApiStatusCodes.PAYMENT_CHANNELS &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        handlePaymentChannels(it.response?.storePaymentChannelList)
                    }
                }

                Status.ERROR -> {
                }
            }
        })

    }

    private fun handlePaymentChannels(storePaymentChannelList: List<StorePaymentChannel>?) {
        if (storePaymentChannelList != null) {
            for (item in storePaymentChannelList) {
                if (item.channelCode == DINARPAY_CODE && item.isEnabled == "Y") {
                    binding.isDinarPayAccepted = true
                }
                if (item.channelCode == SADADPAY_CODE && item.isEnabled == "Y") {
                    binding.isSadadPayAccepted = true
                }
                if (item.channelCode == CASH_DELIVERY_CODE && item.isEnabled == "Y") {
                    binding.isCashAccepted = true
                }
            }
        }
    }
}