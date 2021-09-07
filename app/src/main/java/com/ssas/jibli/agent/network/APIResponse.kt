package com.ssas.jibli.agent.network

class APIResponse<T> {
    var error: Throwable? = null
    var response: T? = null
    var status: Status? = null

    constructor(status: Status?, throwable: Throwable?, `object`: T) {
        error = throwable
        response = `object`
        this.status = status
    }

    constructor() {}

    fun <T> onSuccess(`object`: T): APIResponse<*> {
        return APIResponse<Any?>(Status.SUCCESS, null, `object`)
    }

    fun onError(error: Throwable?): APIResponse<*> {
        return APIResponse<Any?>(Status.ERROR, error, null)
    }

    fun onLoading(): APIResponse<*> {
        return APIResponse<Any?>(Status.LOADING, null, null)
    }
}