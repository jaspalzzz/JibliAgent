package com.ssas.jibli.agent.widgets

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


abstract class BaseRecyclerViewClick<V : RecyclerView.ViewHolder> : RecyclerView.Adapter<V>() {
	
	var listener: ItemClickListener? = null
	
	abstract fun count(): Int
	abstract fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): V
	abstract fun onBindViewHolderMethod(holder: V, position: Int)
	
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): V {
		return onCreateHolderMethod(parent, viewType)
	}
	
	override fun getItemCount(): Int {
		return count()
	}
	
	override fun onBindViewHolder(holder: V, position: Int) {
		holder.itemView.setOnClickListener {
			listener?.onItemClick(this, position)
		}
		onBindViewHolderMethod(holder, position)
	}
	
	fun setItemClickListener(owner: ItemClickListener) {
		if (owner == null) {
			return
		}
		listener = owner
	}
}