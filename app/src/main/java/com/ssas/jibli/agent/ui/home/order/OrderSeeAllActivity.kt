package com.ssas.jibli.agent.ui.home.order

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.databinding.ActivityOrderSeeAllBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.home.adapter.ReviewOrderTransactionAdapter
import com.ssas.jibli.agent.utils.Utils

class OrderSeeAllActivity : BaseActivity<ActivityOrderSeeAllBinding, HomeVM>() {

    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false
    private var isFirstLoading = true
    private var orderAdapter: ReviewOrderTransactionAdapter? = null

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_order_see_all, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        inflateOrderList()
        swipeRefresh()
        searchOrderList()
    }

    private fun setToolbarTitle(title: Int) {
        binding.searchOrderToolbar.toolbarTitle.setText(title)
    }

    private fun initToolbar() {
        setToolbarTitle(R.string.orders_delivery)
        binding.searchOrderToolbar.toolbarBackBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun searchOrderList() {
        viewModel.searchOrderDetails(pageNumber, limit, true, false,"")
    }


    private fun inflateOrderList() {
        var layoutManager = LinearLayoutManager(this@OrderSeeAllActivity)
        binding.searchOrderList.layoutManager = layoutManager
        orderAdapter = ReviewOrderTransactionAdapter(this) { position, item ->
            var bundle = Bundle().apply {
                putString(SharingKeys.ORDER_TRANSACTION_ID,item?.orderTransactionId)
            }
            Utils.jumpActivityForResult(this,RequestCodes.CHANGE_ORDER_REQUEST_CODE,OrderDetailActivity::class.java,bundle)
        }
        binding.searchOrderList.adapter = orderAdapter
        binding.searchOrderList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            orderAdapter?.hideProgressBar(false)
                            searchOrderList()
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

        vm.searchOrder.observe(this, Observer {
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

                    if (responseCode == ApiStatusCodes.SEARCH_CUSTOMER_ORDER_SUCCESS &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        val response = it.response?.orderTransactionArr
                        if (!response.isNullOrEmpty()) {
                            addOrderData(response)
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

    private fun addOrderData(orderList: ArrayList<OrderTransactionArr>) {
        if (orderList.isNullOrEmpty()) {
            if (orderAdapter?.count() == 0) {
                emptyDataLayout()
            } else {
                orderAdapter?.hideProgressBar(true)
            }
        } else {
            mainLayout()
            orderAdapter?.addData(orderList)
        }
    }

    private fun mainLayout() {
        orderAdapter?.hideProgressBar(true)
        loading = true
        binding.isEmptySalesOrder = false
    }

    private fun emptyDataLayout() {
        orderAdapter?.hideProgressBar(true)
        loading = false
        if (orderAdapter?.count()!! <= 0) {
            binding.isEmptySalesOrder = true
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        orderAdapter?.clearData()
        isScrolling = false
        pageNumber = 0
        searchOrderList()
    }

    private fun stopRefreshingData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RequestCodes.CHANGE_ORDER_REQUEST_CODE && resultCode == RequestCodes.CHANGE_ORDER_RESULT_CODE) {
            startRefreshingData()
        }
    }
}