package com.ssas.jibli.agent.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssas.jibli.agent.R
import kotlinx.android.synthetic.main.toast_dialog_layout.*

class ToastDialog : DialogFragment() {
	
	private var image: Int = R.drawable.ic_check

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.toast_dialog_layout, container, false)
	}
	
	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		arguments.let {
			image = it?.getInt(ARG_IMAGE)?:R.drawable.ic_check
			toastImage.setImageResource(image)
		}
	}
	
	override fun onStart() {
		super.onStart()
		val dialog = dialog
		if (dialog != null) {
			dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
		}
	}
	
	companion object {
		val ARG_IMAGE: String = "ARG_IMAGE"
		@JvmStatic
		fun newInstance(image: Int) =
			ToastDialog().apply {
				arguments = Bundle().apply {
					putInt(ARG_IMAGE, image)
				}
			}
	}
	
}
