package com.ssas.jibli.agent.utils

interface PermissionResultListener {
	fun onPermissionResult(
		requestCode: Int,
		permissions: Array<out String>, grantResults: IntArray
	)
}
