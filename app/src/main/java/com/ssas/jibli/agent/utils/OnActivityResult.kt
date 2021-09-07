package com.ssas.jibli.agent.utils

import android.content.Intent


interface OnActivityResult {
	
	fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
}