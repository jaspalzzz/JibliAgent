package com.ssas.jibli.agent.dialogs

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.utils.countrycode.Country
import com.ssas.jibli.agent.utils.countrycode.CountryUtils
import kotlinx.android.synthetic.main.dialog_country_list.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.toolbar.view.*


class CountryPickerDialog : DialogFragment() {

    private lateinit var adapter: CountryPickerAdapter
    private val arrCountryList = ArrayList<Country>()
    lateinit var callback: CountrySelect

    fun setListener(callback: CountrySelect) {
        this.callback = callback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Theme_JibliAgent)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.dialog_country_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arrCountryList.clear()
        CountryUtils.getAllCountries(requireContext())?.let {
            arrCountryList.addAll(it)
        }
        adapter.setData(arrCountryList)
        initToolbar()
    }

    private fun initToolbar() {
        toolbar.toolbarBackBt.setOnClickListener {
            dismiss()
        }
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        toolbarTitle.text = getString(R.string.select_country_code)
    }

    private fun setUpRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        mRvCountry.layoutManager = layoutManager
        adapter = CountryPickerAdapter(requireContext())
        adapter.setListener(callback)
        mRvCountry.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }
    }

    interface CountrySelect {
        fun onCountryClick(country: Country)
    }
}
