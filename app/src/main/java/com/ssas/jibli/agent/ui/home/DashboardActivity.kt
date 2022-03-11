package com.ssas.jibli.agent.ui.home

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.gson.Gson
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.constants.RequestCodes
import com.ssas.jibli.agent.data.constants.SharingKeys
import com.ssas.jibli.agent.data.models.UserProfileArr
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.databinding.ActivityDashboardBinding
import com.ssas.jibli.agent.repo.home.HomeClickEvents
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.auth.LoginActivity
import com.ssas.jibli.agent.ui.auth.dialog.LanguageChangeDialog
import com.ssas.jibli.agent.ui.home.adapter.ReviewOrderTransactionAdapter
import com.ssas.jibli.agent.ui.home.notification.NotificationsActivity
import com.ssas.jibli.agent.ui.home.order.OrderDetailActivity
import com.ssas.jibli.agent.ui.home.order.OrderSeeAllActivity
import com.ssas.jibli.agent.ui.home.shops.ViewShopsActivity
import com.ssas.jibli.agent.utils.BindingImageAdapter
import com.ssas.jibli.agent.utils.LanguageUtils
import com.ssas.jibli.agent.utils.Utils

class DashboardActivity : BaseActivity<ActivityDashboardBinding, HomeVM>() {

    lateinit var appUpdateManager: AppUpdateManager
    private var installStateUpdatedListener: InstallStateUpdatedListener? = null


    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_dashboard, HomeVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        checkForAppUpdate()
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
                    var languageDialog = LanguageChangeDialog{
                        Utils.jumpActivityClearTask(this, DashboardActivity::class.java)
                    }
                    languageDialog.show(supportFragmentManager,"")
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        showProfileDetails()
        checkNewAppVersionState()
    }

    private fun showProfileDetails() {
        var agentProfile: UserProfileArr? = Gson().fromJson<UserProfileArr>(
            prefMain[PrefKeys.AGENT_PROFILE_DETAILS, ""],
            UserProfileArr::class.java
        )
        binding.userName.text = agentProfile?.loginUserName
        binding.agentCode.text = agentProfile?.agentCode

        BindingImageAdapter.setImageBase64WithDefault(
            binding.userImage,
            agentProfile?.profileImage1
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

    private fun checkForAppUpdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo
        // Checks that the platform will allow the specified type of update.
        registerFlexibleListener()

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    startAppUpdateImmediate(appUpdateInfo)
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                    startAppUpdateFlexible(appUpdateInfo)
                }

            }
        }
    }

    private fun startAppUpdateImmediate(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.IMMEDIATE,
                this,
                RequestCodes.APP_UPDATE
            )
        } catch (e: Exception) {
            Log.e("jaspal", "Immediate App update >> " + e.stackTrace)
        }
    }

    private fun startAppUpdateFlexible(appUpdateInfo: AppUpdateInfo) {
        try {
            appUpdateManager.registerListener(installStateUpdatedListener)
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                AppUpdateType.FLEXIBLE,
                this,
                RequestCodes.APP_UPDATE
            )
        } catch (e: Exception) {
            Log.e("jaspal", "Immediate App update >> " + e.stackTrace)
        }
    }

    private fun registerFlexibleListener() {
        installStateUpdatedListener = InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADING) {

            }
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackbarForCompleteUpdate()
            }
            // Log state or install the update
        }
    }

    private fun unregisterFlexibleUpdate() {
        appUpdateManager.unregisterListener(installStateUpdatedListener)

    }

    private fun popupSnackbarForCompleteUpdate() {
        Snackbar.make(
            binding.rootView,
            getString(R.string.update_download_msg),
            Snackbar.LENGTH_INDEFINITE
        ).apply {
            setAction(getString(R.string.reset)) { appUpdateManager.completeUpdate() }
            setActionTextColor(
                ActivityCompat.getColor(
                    this@DashboardActivity,
                    R.color.colorPrimary
                )
            )
            show()
        }
        unregisterFlexibleUpdate()
    }

    private fun checkNewAppVersionState() {
        appUpdateManager
            .appUpdateInfo
            .addOnSuccessListener { appUpdateInfo: AppUpdateInfo ->
                //FLEXIBLE:
                // If the update is downloaded but not installed,
                // notify the user to complete the update.
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    popupSnackbarForCompleteUpdate()
                }

                //IMMEDIATE:
                if (appUpdateInfo.updateAvailability()
                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS
                ) {
                    // If an in-app update is already running, resume the update.
                    startAppUpdateImmediate(appUpdateInfo)
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterFlexibleUpdate()
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

        if (requestCode == RequestCodes.APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                unregisterFlexibleUpdate()
            }
        }

    }
}