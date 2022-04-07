package com.ssas.jibli.agent.ui.home.shops

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.constants.ValConstant
import com.ssas.jibli.agent.data.models.merchantStore.SearchMerchantStoreArr
import com.ssas.jibli.agent.databinding.ActivityViewShopsBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeClickEvents
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.home.adapter.MerchantStoreAdapter
import com.ssas.jibli.agent.utils.Utils

class ViewShopsActivity : BaseActivity<ActivityViewShopsBinding, HomeVM>() {

    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false
    private var isFirstLoading = true
    private lateinit var searchMerchantAdapter: MerchantStoreAdapter
    var selectedTab = SharingKeys.SHOPPING_MALL_TAB


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_view_shops, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        activeTabHandling(true, false, false, false)
        inflateBasketList()
        swipeRefresh()
        searchMerchantStoreList()
    }

    private fun setToolbarTitle(title: Int) {
        binding.merchantToolbar.toolbarTitle.setText(title)
    }

    private fun initToolbar() {
        setToolbarTitle(R.string.my_shops)
        binding.merchantToolbar.toolbarBackBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun searchMerchantStoreList() {
        viewModel.searchMerchantStores(pageNumber, limit, selectedTab)
    }

    private fun inflateBasketList() {
        var layoutManager = LinearLayoutManager(this)
        binding.merchantStoreList.layoutManager = layoutManager
        searchMerchantAdapter = MerchantStoreAdapter { position, item ->
            var bundle = Bundle().apply {
                putParcelable(SharingKeys.MERCHANT_STORE, item)
            }
            Utils.jumpActivityWithData(this, ViewMerchantActivity::class.java, bundle)
        }
        binding.merchantStoreList.adapter = searchMerchantAdapter
        binding.merchantStoreList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if (dy > 0) {
                    if (loading) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                            loading = false
                            pageNumber += 1
                            isScrolling = true
                            isFirstLoading = false
                            searchMerchantAdapter.hideProgressBar(false)
                            searchMerchantStoreList()
                        }
                    }
                }
            }
        })
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
                HomeClickEvents.SHOPPING_TAB_CLICK -> {
                    activeTabHandling(true, false, false, false)
                    selectedTab = SharingKeys.SHOPPING_MALL_TAB
                    startRefreshingData()

                }

                HomeClickEvents.WATER_TAB_CLICK -> {
                    activeTabHandling(false, false, true, false)
                    selectedTab = SharingKeys.WATER_TAB
                    startRefreshingData()
                }

                HomeClickEvents.GAS_TAB_CLICK -> {
                    activeTabHandling(false, true, false, false)
                    selectedTab = SharingKeys.GAS_TAB
                    startRefreshingData()
                }

                HomeClickEvents.FOOD_TAB_CLICK -> {
                    activeTabHandling(false, false, false, true)
                    selectedTab = SharingKeys.FOOD_TAB
                    startRefreshingData()
                }
            }
        })

        vm.searchMerchantStoresResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    if (isFirstLoading) {
                        binding.swipeRefresh.isRefreshing = true
                    }
                }

                Status.SUCCESS -> {
                    stopRefreshingData()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == ApiStatusCodes.SEARCH_MERCHANT_STORE &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        val response = it.response?.searchMerchantStoreArr
                        if (!response.isNullOrEmpty()) {
                            addMerchantStoreData(response)
                        } else {
                            alertDialogShow(this, it.response?.responseCodeVO?.responseValue ?: "")
                        }
                    } else {
                        emptyDataLayout()
                    }
                }

                Status.ERROR -> {
                    startRefreshingData()
                    emptyDataLayout()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })
    }

    private fun addMerchantStoreData(merchantStoreList: ArrayList<SearchMerchantStoreArr>) {
        if (merchantStoreList.isNullOrEmpty()) {
            if (searchMerchantAdapter.count() == 0) {
                emptyDataLayout()
            } else {
                searchMerchantAdapter.hideProgressBar(true)
            }
        } else {
            mainLayout()
            searchMerchantAdapter.addData(merchantStoreList)
        }
    }

    private fun mainLayout() {
        searchMerchantAdapter.hideProgressBar(true)
        loading = true
        binding.isEmptyMerchantStore = false
    }

    private fun emptyDataLayout() {
        searchMerchantAdapter.hideProgressBar(true)
        loading = false
        if (searchMerchantAdapter.count() <= 0) {
            binding.isEmptyMerchantStore = true
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        searchMerchantAdapter.clearData()
        isScrolling = false
        pageNumber = 0
        searchMerchantStoreList()
    }

    private fun stopRefreshingData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun activeTabHandling(
        isShop: Boolean,
        isGas: Boolean,
        isWater: Boolean,
        isFood: Boolean
    ) {
        binding.isShoppingAct = isShop
        binding.isGasAct = isGas
        binding.isWaterAct = isWater
        binding.isFoodAct = isFood
    }
}