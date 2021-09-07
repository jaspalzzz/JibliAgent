package com.ssas.jibli.agent.dialogs

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.databinding.ItemCountryBinding
import com.ssas.jibli.agent.utils.countrycode.Country
import java.util.*


class CountryPickerAdapter(internal var context: Context) :
    RecyclerView.Adapter<CountryPickerAdapter.Holder>() {

    var filteredData: ArrayList<Country> = ArrayList()
    var response: ArrayList<Country> = ArrayList()

    lateinit var callback: CountryPickerDialog.CountrySelect

    fun setListener(callback: CountryPickerDialog.CountrySelect) {
        this.callback = callback
    }

    fun setData(response: ArrayList<Country>?) {
        this.filteredData.addAll(response!!)
        this.response.addAll(response)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): Holder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemCountryBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_country, p0, false)
        return Holder(
            binding
        )
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.binding.data = filteredData[position]
        holder.binding.callBack = callback
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return filteredData.size
    }

    fun clearData() {
        filteredData.clear()
        notifyDataSetChanged()
    }

    class Holder(var binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root)
}