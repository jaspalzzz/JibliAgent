package com.ssas.jibli.agent.ui.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.transition.Hold
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.models.searchOrder.CustomerOrderDetailsList
import com.ssas.jibli.agent.data.models.searchOrder.OrderTransactionArr
import com.ssas.jibli.agent.databinding.ItemOrderProductBinding
import com.ssas.jibli.agent.widgets.BaseRecyclerViewClick

class OrderProductAdapter : RecyclerView.Adapter<OrderProductAdapter.Holder>() {

    var productList = ArrayList<CustomerOrderDetailsList>()

    class Holder(var binding:ItemOrderProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var inflater = LayoutInflater.from(parent.context)
        var binding = DataBindingUtil.inflate<ItemOrderProductBinding>(inflater, R.layout.item_order_product,parent,false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.productNameText.text = productList[position].productName
        holder.binding.productQuantityText.text = productList[position].totalQuantity+" "+productList[position].unitOfProduct
        holder.binding.productAmountText.text = productList[position].maximumRetailPrice+" "+productList[position].currencyCode
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
       return productList.size
    }

    fun addData(list:ArrayList<CustomerOrderDetailsList>){
        productList = list
        notifyDataSetChanged()
    }
}