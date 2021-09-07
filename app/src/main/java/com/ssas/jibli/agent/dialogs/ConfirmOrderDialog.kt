package com.ssas.jibli.agent.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ssas.jibli.agent.R
import kotlinx.android.synthetic.main.confirm_delivery_dialog.*


class ConfirmOrderDialog : DialogFragment() {


    private var orderTransactionID: String = ""
    private var icon = R.drawable.ic_scoter
    private var title:String = ""
    private var message:String = ""

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
        return inflater.inflate(R.layout.confirm_delivery_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window!!.attributes.windowAnimations = R.style.AlertDialogAnimation
        if (arguments != null) {
            arguments?.let {
                orderTransactionID = it.getString(ARG_ORDER_TRANSACTION_ID) ?: ""
                icon = it?.getInt(ARG_ICON)
                title = it?.getString(ARG_TITLE)?:""
                message = it?.getString(ARG_MESSAGE)?:""
            }
        }
        init()
    }

    private fun init() {
        dialog?.dialogTitle?.text = title
        dialog?.dialog_msg?.text = message
        dialog?.dialog_orderId?.text = orderTransactionID
        dialog?.dialog_icon?.setImageResource(icon)
        dialog?.dialog_alert_bt?.setOnClickListener {
            lis.onPositiveButtonClick(this)
        }
        dialog?.cancelBt?.setOnClickListener {
            lis.onCancelButtonClick(this)
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
        fun onPositiveButtonClick(dialog: ConfirmOrderDialog)
        fun onCancelButtonClick(dialog: ConfirmOrderDialog)
    }


    companion object {
        const val ARG_ORDER_TRANSACTION_ID: String = "ARG_ORDER_TRANSACTION_ID"
        const val ARG_TITLE = "ARG_TITLE"
        const val ARG_ICON = "ARG_ICON"
        const val ARG_MESSAGE = "ARG_MESSAGE"

        @JvmStatic
        fun newInstance(orderTransactionId: String,icon:Int,title:String,message:String) =
            ConfirmOrderDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_ORDER_TRANSACTION_ID, orderTransactionId)
                    putInt(ARG_ICON,icon)
                    putString(ARG_TITLE,title)
                    putString(ARG_MESSAGE,message)
                }
            }
    }

}