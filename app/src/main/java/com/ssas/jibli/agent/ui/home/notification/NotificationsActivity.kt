package com.ssas.jibli.agent.ui.home.notification

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.databinding.ActivityNotificationsBinding
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.home.adapter.SearchNotificationAdapter
import com.ssas.jibli.agent.data.models.searchNotification.NotificationHistory
import com.ssas.jibli.agent.utils.Utils

class NotificationsActivity : BaseActivity<ActivityNotificationsBinding,HomeVM>() {

    private var pageNumber = 0
    private var limit = 10
    private var loading = true
    private var isScrolling = false
    private var isFirstLoading = true
    private var notificationAdapter: SearchNotificationAdapter? = null

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_notifications,HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
        inflateNotificationList()
        swipeRefresh()
        searchNotificationList()
    }

    private fun setToolbarTitle(title: Int) {
        binding.notificationToolbar.toolbarTitle.setText(title)
    }

    private fun initToolbar() {
        setToolbarTitle(R.string.view_notifications)
        binding.notificationToolbar.toolbarBackBt.setOnClickListener {
            onBackPressed()
        }
    }

    private fun searchNotificationList(){
        viewModel.searchNotificationHistory(limit,pageNumber)
    }

    private fun inflateNotificationList() {
        var layoutManager = LinearLayoutManager(this@NotificationsActivity)
        binding.searhNotificationList.layoutManager = layoutManager
        notificationAdapter = SearchNotificationAdapter()
        binding.searhNotificationList.adapter = notificationAdapter
        binding.searhNotificationList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                            notificationAdapter?.hideProgressBar(false)
                            searchNotificationList()
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

        vm.searchNotificationHistoryResponse.observe(this, Observer {
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

                    if (responseCode == ApiStatusCodes.SEARCH_NOTIFICATION &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        val response = it.response?.notificationHistoryList
                        if (!response.isNullOrEmpty()) {
                            addNotificationData(response)
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

    private fun addNotificationData(notificationList: ArrayList<NotificationHistory>) {
        if (notificationList.isNullOrEmpty()) {
            if (notificationAdapter?.count() == 0) {
                emptyDataLayout()
            } else {
                notificationAdapter?.hideProgressBar(true)
            }
        } else {
            mainLayout()
            notificationAdapter?.addData(notificationList)
        }
    }

    private fun mainLayout() {
        notificationAdapter?.hideProgressBar(true)
        loading = true
        binding.isEmptyNotification = false
    }

    private fun emptyDataLayout() {
        notificationAdapter?.hideProgressBar(true)
        loading = false
        if (notificationAdapter?.count()!! <= 0) {
            binding.isEmptyNotification = true
        }
    }

    private fun swipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            startRefreshingData()
        }
    }

    private fun startRefreshingData() {
        notificationAdapter?.clearData()
        isScrolling = false
        pageNumber = 0
        searchNotificationList()
    }

    private fun stopRefreshingData() {
        if (binding.swipeRefresh.isRefreshing) {
            binding.swipeRefresh.isRefreshing = false
        }
    }
}