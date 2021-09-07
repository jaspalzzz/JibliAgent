package com.ssas.jibli.agent.repo.auth

import com.google.gson.JsonObject
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.data.models.CommonResponse
import com.ssas.jibli.agent.data.models.LoginResponse
import com.ssas.jibli.agent.network.ApiService
import retrofit2.http.Body
import javax.inject.Inject

class AuthRepo {

    @Inject
    lateinit var apiService: ApiService

    init {
        MApplication.netComponents.inject(this)
    }

     suspend fun loginRequest(params: JsonObject): LoginResponse{
        return apiService.loginRequest(params)
    }

    suspend fun retrieveUserPassword(params: JsonObject): CommonResponse{
        return apiService.retrieveUserPassword(params)
    }

}
