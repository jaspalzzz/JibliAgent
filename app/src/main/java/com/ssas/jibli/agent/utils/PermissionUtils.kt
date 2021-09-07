package com.ssas.jibli.agent.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

object PermissionUtils {
	const val PERMISSION_LOCATION_REQUEST_CODE = 200

	var LOCATION_PERMISSION=arrayOf(Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)

	@JvmStatic
	fun hasPermission(context: Context?, permissions: Array<String>): Boolean {
		if (context != null && permissions != null) {
			for (permission in permissions) {
				if (ActivityCompat.checkSelfPermission(
						context,
						permission
					) != PackageManager.PERMISSION_GRANTED
				) {
					return false
				}
			}
		}
		return true
	}
	
	@JvmStatic
	fun requestPermissions(context: Activity, arrayPermission: Array<String>, REQUEST_CODE: Int) {
		ActivityCompat.requestPermissions(
			context, arrayPermission,
			REQUEST_CODE
		)
	}
	
	private fun checkPermissionGranted(context: Context, permissions: Array<String>): Boolean {
		val deniedPermissions = ArrayList<String>()
		for (permission in permissions) {
			if (ActivityCompat.checkSelfPermission(
					context,
					permission
				) == PackageManager.PERMISSION_DENIED
			) {
				deniedPermissions.add(permission)
			}
		}
		return deniedPermissions.isEmpty()
	}

	
}

