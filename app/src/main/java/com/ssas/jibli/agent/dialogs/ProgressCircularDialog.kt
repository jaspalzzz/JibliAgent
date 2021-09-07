package com.ssas.jibli.agent.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssas.jibli.agent.R

class ProgressCircularDialog : DialogFragment() {
	
	val ARG_TITLE: String = "ARG_TITLE"
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_JibliAgent)
	}
	
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.progress_circular_dailog, container, false)
	}
	
	override fun onStart() {
		super.onStart()
		val dialog = dialog
		if (dialog != null) {
			val width = ViewGroup.LayoutParams.MATCH_PARENT
			val height = ViewGroup.LayoutParams.MATCH_PARENT
			dialog.window?.setLayout(width, height)
			isCancelable = false
		}
	}
	
	companion object {
		@JvmStatic
		fun newInstance(title: String) =
			ProgressCircularDialog().apply {
				arguments = Bundle().apply {
					putString(ARG_TITLE, title)
				}
			}
	}
	
}
