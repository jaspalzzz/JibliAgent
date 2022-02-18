package com.ssas.jibli.agent.ui.auth

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.data.models.UserProfileArr
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.databinding.ActivityLoginBinding
import com.ssas.jibli.agent.dialogs.CommonDialog
import com.ssas.jibli.agent.network.ApiStatusCodes
import com.ssas.jibli.agent.network.Status
import com.ssas.jibli.agent.repo.auth.AuthClickEvents
import com.ssas.jibli.agent.repo.auth.AuthVM
import com.ssas.jibli.agent.ui.auth.dialog.LanguageChangeDialog
import com.ssas.jibli.agent.ui.home.DashboardActivity
import com.ssas.jibli.agent.utils.LanguageUtils
import com.ssas.jibli.agent.utils.Utils

class LoginActivity : BaseActivity<ActivityLoginBinding, AuthVM>() {

    private var isAgent = ""
    private var isHidePassword = true

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_login, AuthVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        selectPreviousLanguage()
    }

    private fun selectPreviousLanguage() {
        var language = prefMain.get(PrefKeys.LANGUAGE, LanguageUtils.ARABIC)
        when (language) {
            LanguageUtils.ENGLISH -> {
                binding?.languageChangeText?.text = getString(R.string.en)
            }
            LanguageUtils.ARABIC -> {
                binding?.languageChangeText?.text = getString(R.string.ar)
            }
        }
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
                AuthClickEvents.FORGET_PASSWORD_CLICK -> {
                    Utils.jumpActivity(this@LoginActivity, ForgetPasswordActivity::class.java)
                }

                AuthClickEvents.LOGIN_PASSWORD_TOGGLE -> {
                    handlePasswordToggle()
                }

                AuthClickEvents.SIGN_UP_AGENT->{
                    Utils.openWebLink(this,"https://jibli.ly/become-a-delivery-boy/")
                }

                AuthClickEvents.LANGUAGE_CHANGE_CLICK -> {
                    var languageChangeDialog = LanguageChangeDialog{
                        Utils.jumpActivityClearTask(this, LoginActivity::class.java)
                    }
                    languageChangeDialog.show(supportFragmentManager, languageChangeDialog.tag)
                }
            }
        })

        vm.loginResponse.observe(this, Observer {
            when (it.status) {
                Status.LOADING -> {
                    showProgress()
                }

                Status.SUCCESS -> {
                    dismissProgress()
                    val responseCode = it?.response?.responseCodeVO?.responseCode
                    val responseMessage = it?.response?.responseCodeVO?.responseMessage

                    if (responseCode == ApiStatusCodes.USER_LOGIN_SUCCESS &&
                        responseMessage == ApiStatusCodes.SUCCESS
                    ) {
                        if (!it.response?.userProfileArr.isNullOrEmpty()) {
                            val userArr = it.response?.userProfileArr
                            if (!userArr?.get(0)?.isAgent.isNullOrEmpty()) {
                                saveAgentDetails(userArr?.get(0))
                                Utils.jumpActivityClearTask(this,DashboardActivity::class.java)
                                showToast(this, it.response?.responseCodeVO?.responseValue?:"")
                            } else {
                                showErrorDialog(getString(R.string.not_delivery_agent),getString(R.string.not_delivery_agent_message),true)
                            }
                        }else{
                            showErrorDialog(
                                it.response?.responseCodeVO?.responseMessage?:"",
                                it.response?.responseCodeVO?.responseValue?:"",false)
                        }
                    } else {
                        showErrorDialog(it?.response?.responseCodeVO?.responseMessage?:"",it?.response?.responseCodeVO?.responseValue?:"",false)
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

    private fun showErrorDialog(title:String,message:String,isCustomer:Boolean){
        val dialog = CommonDialog.newInstance(
            title,message,
            R.drawable.ic_account_lock,
            getString(R.string.go_to_play_store)
        )
        dialog.setListener(object : CommonDialog.SuccessDialogListener {
            override fun onPositiveButtonClick(dialog: CommonDialog) {
                dialog.dismiss()
                if(isCustomer){
                    Utils.openPlayStoreApp(this@LoginActivity,"com.ssas.jibli")
                }
            }
        })
        dialog.show(supportFragmentManager, "")
    }

    private fun saveAgentDetails(userProfileArr: UserProfileArr?) {
        var profileDetails  = Gson().toJson(userProfileArr)
        prefMain.put(PrefKeys.SALES_AGENT_CODE,userProfileArr?.agentCode?:"")
        prefMain.put(PrefKeys.AGENT_PROFILE_DETAILS,profileDetails)
    }

    private fun handlePasswordToggle() {
        if (isHidePassword) {
            Utils.hidePassword(binding.passwordEt)
            binding.isToggle = true
            isHidePassword = false
        } else {
            Utils.showPassword(binding.passwordEt)
            binding.isToggle = false
            isHidePassword = true
        }
    }
}