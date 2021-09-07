package com.ssas.jibli.agent.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.databinding.ItemSearchNotificationBinding
import com.ssas.jibli.agent.data.models.searchNotification.NotificationHistory
import com.ssas.jibli.agent.widgets.FooterRecyclerView


class SearchNotificationAdapter : FooterRecyclerView() {
	
	var searchNotificationList = ArrayList<NotificationHistory>()
	val ITEM_VIEW = 1
	
	override fun count(): Int {
		return searchNotificationList.size
	}
	
	override fun viewType(): Int {
		return ITEM_VIEW
	}
	
	override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
		var binding = DataBindingUtil.inflate<ItemSearchNotificationBinding>(
			LayoutInflater.from(parent.context),
			R.layout.item_search_notification,
			parent,
			false
		)
		return Holder(binding)
	}
	
	override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
		if(holder is Holder){
			holder.binding.item = searchNotificationList[position]
			holder.binding.executePendingBindings()
		}
	}
	
	fun addData(notificationList: ArrayList<NotificationHistory>) {
		searchNotificationList.addAll(notificationList)
		notifyDataSetChanged()
	}
	
	fun clearData() {
		searchNotificationList.clear()
		notifyDataSetChanged()
	}
	
	class Holder(var binding: ItemSearchNotificationBinding) : RecyclerView.ViewHolder(binding.root)
}