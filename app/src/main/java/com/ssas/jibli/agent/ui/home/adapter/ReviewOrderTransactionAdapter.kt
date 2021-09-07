package com.ssas.jibli.agent.ui.home.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.constants.ValConstant
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.databinding.OrderTransactionItemBinding
import com.ssas.jibli.agent.databinding.OrderTransactionReviewItemBinding
import com.ssas.jibli.agent.widgets.BaseRecyclerViewClick
import com.ssas.jibli.agent.widgets.FooterRecyclerView

class ReviewOrderTransactionAdapter(var context: Context,var onReviewClick: (position: Int, item: OrderTransactionArr) -> Unit) :
    FooterRecyclerView() {

    private var ITEM_TYPE = 1
    private var hideStatusView = true
    private var orders: MutableList<OrderTransactionArr> = ArrayList()

    class Holder(var binding: OrderTransactionReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return orders.size
    }

    override fun viewType(): Int {
        return ITEM_TYPE
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        return Holder(
            DataBindingUtil.inflate<OrderTransactionReviewItemBinding>(
                LayoutInflater.from(parent.context),
                R.layout.order_transaction_review_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            val order = orders[position]

            holder.binding.storeImage.setImageDrawable(null)
            holder.binding.orderedImage.setImageResource(R.drawable.ic_process)
            holder.binding.acceptedImage.setImageResource(R.drawable.ic_process)
            holder.binding.readyForDevliveryImage.setImageResource(R.drawable.ic_process)
            holder.binding.shippedImage.setImageResource(R.drawable.ic_process)
            holder.binding.deliveredImage.setImageResource(R.drawable.ic_process)

            holder.binding.hideStatusView = hideStatusView

            holder.binding.item = order

            holder.binding.orderRootView.setOnClickListener {
                if (hideStatusView) {
                    holder.binding.hideStatusView = false
                    hideStatusView = false
                } else {
                    holder.binding.hideStatusView = true
                    hideStatusView = true
                }
            }

            if(order.isRegularDelivery == "Y"){
                holder.binding.deliveryType.text = context.getString(R.string.regular)
                holder.binding.deliveryType.setTextColor(ActivityCompat.getColor(context,R.color.colorGreen))
            }
            if(order.isPremiumDelivery == "Y"){
                holder.binding.deliveryType.text = context.getString(R.string.premium)
                holder.binding.deliveryType.setTextColor(ActivityCompat.getColor(context,R.color.colorRed))
            }

            //handle order status code views
            when (order.statusCode) {
                ValConstant.ORDERED -> {
                    holder.binding.isDelivered = false
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processing)
                }

                ValConstant.ACT -> {
                    holder.binding.isDelivered = false
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processing)
                }

                ValConstant.ACCEPTED -> {
                    holder.binding.isDelivered = false
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.readyForDevliveryImage.setImageResource(R.drawable.ic_processing)
                }

                ValConstant.READY_FOR_DELIVERY -> {
                    holder.binding.isDelivered = false
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.readyForDevliveryImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.shippedImage.setImageResource(R.drawable.ic_processing)
                }

                ValConstant.SHIPPED -> {
                    holder.binding.isDelivered = false
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.readyForDevliveryImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.shippedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.deliveredImage.setImageResource(R.drawable.ic_processing)

                }

                ValConstant.DELIVERED -> {
                    holder.binding.isDelivered = true
                    holder.binding.orderedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.acceptedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.readyForDevliveryImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.shippedImage.setImageResource(R.drawable.ic_processed)
                    holder.binding.deliveredImage.setImageResource(R.drawable.ic_processed)
                }
            }

            holder.binding.reviewOrderBt.setOnClickListener {
                onReviewClick(position, order)
            }

            holder.binding.executePendingBindings()
        }
    }

    fun addData(orders: MutableList<OrderTransactionArr>) {
        this.orders.addAll(orders)
        notifyDataSetChanged()
    }

    fun clearData() {
        if (!orders.isNullOrEmpty()) {
            this.orders.clear()
            notifyDataSetChanged()
        }
    }
}
