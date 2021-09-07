package com.ssas.jibli.agent.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.constants.ValConstant
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.databinding.OrderTransactionItemBinding
import com.ssas.jibli.agent.widgets.BaseRecyclerViewClick

class OrderTransactionAdapter(val context: Context) :
	BaseRecyclerViewClick<OrderTransactionAdapter.Holder>() {

	private var hideStatusView = true
	private var orders: MutableList<OrderTransactionArr> = ArrayList()

	class Holder(var binding: OrderTransactionItemBinding) :
		RecyclerView.ViewHolder(binding.root)

	//return 10 order
	override fun count(): Int {
		return orders.size
	}

	override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
		return Holder(
			DataBindingUtil.inflate<OrderTransactionItemBinding>(
				LayoutInflater.from(parent.context),
				R.layout.order_transaction_item,
				parent,
				false
			)
		)
	}

	@SuppressLint("SetTextI18n")
	override fun onBindViewHolderMethod(holder: Holder, position: Int) {
		val order = orders[position]

		holder.binding.hideStatusView = hideStatusView
		holder.binding.orderRootView.setOnClickListener {
			if (hideStatusView) {
				holder.binding.hideStatusView = false
				hideStatusView = false
			} else {
				holder.binding.hideStatusView = true
				hideStatusView = true
			}
		}

		//handle order status code views
		when (order.statusCode) {
			ValConstant.ORDERED -> {
				holder.binding.orderedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.acceptedImage.setImageResource(R.drawable.ic_order_status)
			}

			ValConstant.ACT -> {
				holder.binding.orderedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.acceptedImage.setImageResource(R.drawable.ic_order_status)
			}

			ValConstant.ACCEPTED -> {
				holder.binding.orderedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.acceptedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.shippedImage.setImageResource(R.drawable.ic_order_status)
			}

			ValConstant.SHIPPED -> {
				holder.binding.orderedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.acceptedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.shippedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.deliveredImage.setImageResource(R.drawable.ic_order_status)

			}

			ValConstant.DELIVERED -> {
				holder.binding.orderedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.acceptedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.shippedImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
				holder.binding.deliveredImage.setImageResource(R.drawable.ic_check_circle_black_24dp)
			}
		}

		holder.binding.item = order
		holder.binding.executePendingBindings()
	}

	fun addData(orders: MutableList<OrderTransactionArr>) {
		this.orders.clear()
		this.orders.addAll(orders)
		notifyDataSetChanged()
	}

	fun clearData() {
		this.orders.clear()
		notifyDataSetChanged()
	}
}
