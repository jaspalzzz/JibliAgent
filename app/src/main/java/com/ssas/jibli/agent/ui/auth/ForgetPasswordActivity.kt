package com.ssas.jibli.agent.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.databinding.ActivityForgetPasswordBinding
import com.ssas.jibli.agent.dialogs.CommonDialog
import com.ssas.jibli.agent.dialogs.CountryPickerDialog
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.auth.AuthClickEvents
import com.ssas.jibli.agent.repo.auth.AuthVM
import com.ssas.jibli.agent.utils.Utils
import com.ssas.jibli.agent.utils.countrycode.Country

class ForgetPasswordActivity : BaseActivity<ActivityForgetPasswordBinding, AuthVM>() {

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_forget_password, AuthVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initToolbar()
    }

    private fun initToolbar() {
        binding?.forgetPassToolbar?.toolbarTitle.text = getString(R.string.retrieve_password)
        binding?.forgetPassToolbar?.toolbarBackBt.visibility = View.GONE
    }

    override fun subscribeToEvents(vm: AuthVM) {
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
                AuthClickEvents.COUNTRY_CODE_CLICK -> {
                    countryPickerDialog()
                }
                AuthClickEvents.CANCEL_CLICK -> {
                    finish()
                }
            }
        })

        vm.retrievePasswordResponse.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> {
                    showProgress()
                }
                Status.SUCCESS -> {
                    dismissProgress()
                    if (it?.response?.responseCodeVO?.responseCode == "USRF10007") {
                        var successDialog = CommonDialog.newInstance("",getString(R.string.check_your_mailbox),
                        R.drawable.ic_sent_email,getString(R.string.ok))
                        successDialog.setListener(object :CommonDialog.SuccessDialogListener{
                            override fun onPositiveButtonClick(dialog: CommonDialog) {
                                dialog.dismiss()
                                finish()
                            }
                        })
                        successDialog.show(supportFragmentManager,"")
                    } else {
                        alertDialogShow(this, it?.response?.responseCodeVO?.responseValue ?: "")
                    }
                }
                Status.ERROR -> {
                    dismissProgress()
                    val errorObject = Utils.errorHandlingWithStatus(this, it.error)
                    alertDialogShow(this, errorObject.message)
                }
            }
        })
    }


    private fun countryPickerDialog() {
        var dialog = CountryPickerDialog()
        dialog.setListener(object : CountryPickerDialog.CountrySelect {
            override fun onCountryClick(country: Country) {
                viewModel.countryCode.set(country.phoneCode)
                dialog.dismiss()
            }
        })
        dialog.show(supportFragmentManager, "")
    }
}