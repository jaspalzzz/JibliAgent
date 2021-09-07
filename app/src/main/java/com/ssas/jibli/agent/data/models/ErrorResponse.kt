package com.ssas.jibli.agent.data.models

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
	@SerializedName("message")
	var message: String,
	@SerializedName("statusCode")
	var statusCode: String
)