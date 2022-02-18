package com.ssas.jibli.agent.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.UserProfileArr
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.databinding.ActivityDashboardBinding
import com.ssas.jibli.agent.dialogs.LanguageChangeDialog
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
import com.ssas.jibli.agent.utils.LanguageUtils
import com.ssas.jibli.agent.utils.Utils

class DashboardActivity : BaseActivity<ActivityDashboardBinding, HomeVM>() {

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_dashboard, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        selectPreviousLanguage()
    }

    private fun selectPreviousLanguage() {
        var language = prefMain.get(PrefKeys.LANGUAGE, LanguageUtils.ARABIC)
        when (language) {
            LanguageUtils.ENGLISH -> {
                binding.languageChangeText.text = getString(R.string.en)
            }
            LanguageUtils.ARABIC -> {
                binding.languageChangeText.text = getString(R.string.ar)
            }
        }
    }


    private fun openOrderReview(orderTransactionId:String){
        var bundle = Bundle().apply {
            putString(SharingKeys.ORDER_TRANSACTION_ID, orderTransactionId)
        }
        Utils.jumpActivityForResult(
            this,
            RequestCodes.CHANGE_ORDER_REQUEST_CODE,
            OrderDetailActivity::class.java, bundle
        )
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
                HomeClickEvents.PICKUP_DELIVERIES ->{
                    Utils.jumpActivityForResult(this,BarCodeScannerActivity::class.java,RequestCodes.BAR_CODE_SCAN_REQUEST_CODE)
                }
                HomeClickEvents.MY_ORDERS ->{
                    Utils.jumpActivity(this,OrderSeeAllActivity::class.java)
                }
                HomeClickEvents.LANGUAGE_CHANGE_CLICK ->{
                    var languageChangeDialog = LanguageChangeDialog()
                    languageChangeDialog.show(supportFragmentManager, languageChangeDialog.tag)
                }
            }
        })

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
        mActivityResultListener?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodes.BAR_CODE_SCAN_REQUEST_CODE && resultCode == RequestCodes.QR_RESULT_CODE) {
            if (data != null) {
                var barCode = data.getStringExtra(SharingKeys.QR_RESULT)
               openOrderReview(barCode?:"")

            } else {
                alertDialogShow(this, getString(R.string.alert), getString(R.string.exception_msg))
            }
        }

    }
}