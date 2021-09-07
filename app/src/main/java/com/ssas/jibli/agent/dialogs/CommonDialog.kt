package com.ssas.jibli.agent.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssas.jibli.agent.R
import kotlinx.android.synthetic.main.common_dialog.*




class CommonDialog : DialogFragment() {

	var title = ""
	var message = ""
	var icon = R.drawable.ic_scoter
	var buttonTitle = ""
	
	lateinit var lis: SuccessDialogListener
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AlertDialogStyle)
		
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return inflater.inflate(R.layout.common_dialog, container, false)
	}
	
	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)
		dialog?.window!!.attributes.windowAnimations = R.style.AlertDialogAnimation
		if (arguments != null) {
			arguments?.let {
				title = it.getString(ARG_TITLE)?:""
				message = it.getString(ARG_MESSAGE).toString()
				icon = it.getInt(ARG_ICON)
				buttonTitle = it.getString(ARG_BUTTON_TITLE).toString()
			}
		}
		init()
	}
	
	private fun init() {
		dialog?.dialog_icon?.setImageResource(icon)
		dialog?.dialog_title?.text = title
		dialog?.dialog_msg?.text = message
		dialog?.dialog_alert_bt?.text = buttonTitle
		dialog?.dialog_alert_bt?.setOnClickListener {
			lis.onPositiveButtonClick(this)
		}
	}
	
	override fun onStart() {
		super.onStart()
		if (dialog != null) {
			dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
			isCancelable = false
		}
	}
	
	fun setListener(listener: SuccessDialogListener) {
		this.lis = listener
	}
	
	interface SuccessDialogListener {
		fun onPositiveButtonClick(dialog: CommonDialog)
		fun onCancelButtonClick(dialog: CommonDialog){

		}
	}
	
	
	companion object {
		const val ARG_TITLE: String = "ARG_TITLE"
		const val ARG_MESSAGE: String = "ARG_MESSAGE"
		const val ARG_ICON: String = "ARG_ICON"
		const val ARG_BUTTON_TITLE = "ARG_BUTTON_TITLE"
		@JvmStatic
		fun newInstance(title:String,message: String, icon: Int, buttonTitle: String) =
			CommonDialog().apply {
				arguments = Bundle().apply {
					putString(ARG_TITLE,title)
					putString(ARG_MESSAGE, message)
					putInt(ARG_ICON, icon)
					putString(ARG_BUTTON_TITLE, buttonTitle)
				}
			}
	}
	
}