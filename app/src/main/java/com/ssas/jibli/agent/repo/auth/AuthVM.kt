package com.ssas.jibli.agent.repo.auth

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ssas.jibli.agent.BuildConfig
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.data.models.CommonResponse
import com.ssas.jibli.agent.data.models.LoginResponse
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.network.APIResponse
import com.ssas.jibli.agent.network.NetworkEndPoints
import com.ssas.jibli.agent.utils.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import java.lang.Exception
import javax.inject.Inject

class AuthVM(application: Application) : AndroidViewModel(application) {

    @Inject
    lateinit var prefMain: PrefMain

    init {
        MApplication.appComponent.inject(this)
    }

    var repo = AuthRepo()
    var clickEvents = MutableLiveData<AuthClickEvents>()

    /*Network error*/
    var networkError = MutableLiveData<Boolean>()

    /*
    * variables declaration
    * */

    var email = ObservableField<String>().apply {
        set("")
    }

    var password = ObservableField<String>().apply {
        set("")
    }

    var countryCode = ObservableField<String>().apply {
        set("")
    }

    var phoneNumber = ObservableField<String>().apply {
        set("")
    }

    /*
    * Event Methods
    * */
    fun onLoginPasswordToggleClick() {
        clickEvents.value = AuthClickEvents.LOGIN_PASSWORD_TOGGLE
    }

    fun onForgetPasswordClick() {
        clickEvents.value = AuthClickEvents.FORGET_PASSWORD_CLICK
    }

    fun cancelClick() {
        clickEvents.value = AuthClickEvents.CANCEL_CLICK
    }

    fun countryCodeClick() {
        clickEvents.value = AuthClickEvents.COUNTRY_CODE_CLICK
    }

    fun signUpTextClick(){
        clickEvents.value = AuthClickEvents.SIGN_UP_AGENT
    }

    fun onLanguageChangeClick(){
        clickEvents.value = AuthClickEvents.LANGUAGE_CHANGE_CLICK
    }

    /*
    * Sales agent login request
    * */
    var loginResponse = MutableLiveData<APIResponse<LoginResponse>>()
    fun loginRequest() {
        if (Utils.isInternet(getApplication())) {
            loginResponse.postValue(APIResponse<LoginResponse>().onLoading() as APIResponse<LoginResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("isCustomer", "Y")
            params.addProperty("emailAddress", email.get().toString().trim())
            params.addProperty("loginPassword", password.get().toString().trim())
            params.addProperty("lastLoginMode", "EMAIL")
            params.addProperty("agreedTermsOfUsage", "Y")
            params.addProperty("deviceToken", prefMain[PrefKeys.FIREBASE_TOKEN, ""])
            params.addProperty("deviceType", BuildConfig.REQUEST_CHANNEL)
            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = repo.loginRequest(params)
                    loginResponse.postValue(APIResponse<LoginResponse>().onSuccess(response) as APIResponse<LoginResponse>?)
                } catch (e: Exception) {
                    loginResponse.postValue(APIResponse<LoginResponse>().onError(e) as APIResponse<LoginResponse>)
                }
            }
        } else {
            networkError.value = true
        }
    }


    /*
     * Forget password request
     * */
    var retrievePasswordResponse = MutableLiveData<APIResponse<CommonResponse>>()
    fun retrievePasswordRequest() {
        if (Utils.isInternet(getApplication())) {
            retrievePasswordResponse.postValue(APIResponse<CommonResponse>().onLoading() as APIResponse<CommonResponse>)
            var params = NetworkEndPoints.authJsonObject()
            params.addProperty("isForgetPassword", "Y")
            params.addProperty("mobileNumberCountryCode", countryCode.get().toString().trim())
            params.addProperty("mobileNumber", phoneNumber.get().toString().trim())

            viewModelScope.async(Dispatchers.IO) {
                try {
                    var response = repo.retrieveUserPassword(params)
                    retrievePasswordResponse.postValue(
                        APIResponse<CommonResponse>().onSuccess(
                            response
                        ) as APIResponse<CommonResponse>?
                    )
                } catch (e: Exception) {
                    retrievePasswordResponse.postValue(APIResponse<CommonResponse>().onError(e) as APIResponse<CommonResponse>)
                }
            }
        } else {
            networkError.value = true
        }
    }

}