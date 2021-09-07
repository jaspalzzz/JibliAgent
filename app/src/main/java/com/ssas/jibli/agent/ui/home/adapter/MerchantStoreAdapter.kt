package com.ssas.jibli.agent.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.data.models.merchantStore.SearchMerchantStoreArr
import com.ssas.jibli.agent.databinding.ItemMerchantStoreBinding
import com.ssas.jibli.agent.widgets.FooterRecyclerView


class MerchantStoreAdapter(var onItemClick: (position: Int, item: SearchMerchantStoreArr) -> Unit) :
    FooterRecyclerView() {

    private var merchantStoresList: MutableList<SearchMerchantStoreArr> = ArrayList()
    val ITEM_VIEW = 1


    class Holder(var binding: ItemMerchantStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun count(): Int {
        return merchantStoresList.size
    }

    override fun viewType(): Int {
        return ITEM_VIEW
    }

    override fun onCreateHolderMethod(parent: ViewGroup, viewType: Int): Holder {
        return MerchantStoreAdapter.Holder(
            DataBindingUtil.inflate<ItemMerchantStoreBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_merchant_store,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolderMethod(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is Holder) {
            var vh = holder.binding
            var store = merchantStoresList[position]
            vh.item = store
            vh.root.setOnClickListener {
                onItemClick(position, store)
            }
        }
    }

    fun addData(merchantStores: MutableList<SearchMerchantStoreArr>) {
        this.merchantStoresList.addAll(merchantStores)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        merchantStoresList.removeAt(position)
        notifyDataSetChanged()
    }

    fun getItem(position: Int) = merchantStoresList[position]

    fun clearData() {
        this.merchantStoresList.clear()
        notifyDataSetChanged()
    }
}