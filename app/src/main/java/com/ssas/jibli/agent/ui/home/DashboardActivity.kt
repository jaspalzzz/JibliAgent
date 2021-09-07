package com.ssas.jibli.agent.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.UserProfileArr
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.databinding.ActivityDashboardBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeClickEvents
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.auth.LoginActivity
import com.ssas.jibli.agent.ui.home.adapter.OrderTransactionAdapter
import com.ssas.jibli.agent.ui.home.adapter.ReviewOrderTransactionAdapter
import com.ssas.jibli.agent.ui.home.notification.NotificationsActivity
import com.ssas.jibli.agent.ui.home.order.OrderDetailActivity
import com.ssas.jibli.agent.ui.home.order.OrderSeeAllActivity
import com.ssas.jibli.agent.ui.home.shops.ViewShopsActivity
import com.ssas.jibli.agent.utils.BindingImageAdapter
import com.ssas.jibli.agent.utils.Utils

class DashboardActivity : BaseActivity<ActivityDashboardBinding, HomeVM>() {

    lateinit var searchOrderAdapter: ReviewOrderTransactionAdapter
    private var isFirstLoading = true


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_dashboard, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        inflateSearchOrders()
        swipeRefresh()
        showOrderDetails()
    }

    private fun inflateSearchOrders() {
        searchOrderAdapter = ReviewOrderTransactionAdapter(this) { position, item ->
            var bundle = Bundle().apply {
                putString(SharingKeys.ORDER_TRANSACTION_ID, item?.orderTransactionId)
            }
            Utils.jumpActivityForResult(
                this,
                RequestCodes.CHANGE_ORDER_REQUEST_CODE,
                OrderDetailActivity::class.java, bundle
            )
        }
        binding.transactionList.adapter = searchOrderAdapter
    }

    private fun showOrderDetails() {
        viewModel.searchOrderDetails(0, 10, false, false, "")
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            binding.isEmptyOrders = false
            showOrderDetails()
        }
    }

    private fun stopSwipeRefreshing() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
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
                HomeClickEvents.NOTIFICATIONS_CLICK -> {
                    Utils.jumpActivity(this, NotificationsActivity::class.java)
                }
                HomeClickEvents.ORDER_SEE_ALL_CLICK -> {
                    Utils.jumpActivity(this, OrderSeeAllActivity::class.java)
                }
                HomeClickEvents.VIEW_SHOPS_CLICK -> {
                    Utils.jumpActivity(this, ViewShopsActivity::class.java)
                }
                HomeClickEvents.LOGOUT_BUTTON -> {
                   logoutFromApp()
                }
            }
        })


        vm.searchOrder.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    stopSwipeRefreshing()
                    isFirstLoading = false
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == ApiStatusCodes.SEARCH_CUSTOMER_ORDER_SUCCESS &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        if (!it.response?.orderTransactionArr.isNullOrEmpty()) {
                            searchOrderAdapter.clearData()
                            searchOrderAdapter.addData(it.response?.orderTransactionArr!!)
                        } else {
                            emptyOrderTransactions()
                        }
                    } else {
                        emptyOrderTransactions()
                        alertDialogShow(this, it?.response?.responseCodeVO?.responseValue ?: "")
                    }
                }

                Status.LOADING -> {
                    if (isFirstLoading) {
                        binding.swipeRefresh.isRefreshing = true
                    }
                }

                Status.ERROR -> {
                    stopSwipeRefreshing()
                    isFirstLoading = false
                    emptyOrderTransactions()
                    var errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })

    }

    private fun emptyOrderTransactions() {
        binding.isEmptyOrders = true
    }

    override fun onResume() {
        super.onResume()
        showProfileDetails()
    }

    private fun showProfileDetails() {
        var agentProfile: UserProfileArr? = Gson().fromJson<UserProfileArr>(
            prefMain[PrefKeys.AGENT_PROFILE_DETAILS, ""],
            UserProfileArr::class.java
        )
        binding.userName.text = agentProfile?.loginUserName
        binding.agentCode.text = agentProfile?.agentCode

        BindingImageAdapter.setNetworkImage(
            binding.userImage,
            agentProfile?.profileImage1,
            R.drawable.ic_scoter
        )
    }

    private fun logoutFromApp(){
        alertDialogShow(this,
            getString(R.string.alert),
            getString(R.string.logout_from_app),
            getString(R.string.logout),
            DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
                prefMain.delete(PrefKeys.SALES_AGENT_CODE)
                prefMain.delete(PrefKeys.AGENT_PROFILE_DETAILS)
                Utils.jumpActivityClearTask(this,LoginActivity::class.java)
            }, DialogInterface.OnClickListener { dialog, which ->
                dialog.dismiss()
            })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.CHANGE_ORDER_REQUEST_CODE && resultCode == RequestCodes.CHANGE_ORDER_RESULT_CODE) {
            binding.isEmptyOrders = false
            showOrderDetails()
        }
    }

}