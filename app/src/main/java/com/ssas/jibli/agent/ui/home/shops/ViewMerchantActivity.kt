package com.ssas.jibli.agent.ui.home.shops

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.merchantStore.SearchMerchantStoreArr
import com.ssas.jibli.agent.data.models.paymentChannel.StorePaymentChannel
import com.ssas.jibli.agent.databinding.ActivityViewMerchantBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeVM

class ViewMerchantActivity : BaseActivity<ActivityViewMerchantBinding, HomeVM>(),
    OnMapReadyCallback {

    private var DINARPAY_CODE = "PGC002_DINARPAY_CARD_BUSINESS"
    private var SADADPAY_CODE = "PGC004_SADADPAY_CARD"
    private var CASH_DELIVERY_CODE = "PGC001_CASH_ON_DELIVERY"
    private var map: GoogleMap? = null
    var merchantStore: SearchMerchantStoreArr? = null


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_view_merchant, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        handleIntent(intent)
        initMap()
    }

    private fun handleIntent(intent: Intent?) {
        intent.let {
            merchantStore= it?.getParcelableExtra<SearchMerchantStoreArr>(SharingKeys.MERCHANT_STORE)
            if (merchantStore != null) {
                binding.item = merchantStore
                setToolbarTitle(merchantStore?.storeName ?: "")
                getPaymentChannels(merchantStore?.storeCode)
            }
        }
    }

    private fun getPaymentChannels(storeCode: String?) {
        viewModel.getStorePaymentChannels(storeCode ?: "")
    }

    private fun initMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setToolbarTitle(title: String) {
        binding.storeToolbar.toolbarTitle.text = title
    }

    private fun initToolbar() {
        binding.storeToolbar.toolbarBackBt.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap
        merchantStore?.let { createShopMarker(it) }
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

    private fun createShopMarker(storeData: SearchMerchantStoreArr) {
        if (!storeData.mstLatitude.isNullOrEmpty() &&
            !storeData.mstLongitude.isNullOrEmpty() &&
            !storeData.storeCode.isNullOrEmpty()
        ) {
            val latLng = LatLng(storeData.mstLatitude.toDouble(), storeData.mstLongitude.toDouble())
            val markerOptions = MarkerOptions()
                .title(storeData.storeName)
                .snippet(storeData.storeAddress)
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker())
            map?.addMarker(markerOptions)
            map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12f))
        }
    }
}