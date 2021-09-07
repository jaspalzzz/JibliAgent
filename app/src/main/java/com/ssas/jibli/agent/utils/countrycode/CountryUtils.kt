package com.ssas.jibli.agent.utils.countrycode

import android.content.Context
import android.util.Log
import com.ssas.jibli.agent.R
import java.util.*
import kotlin.collections.ArrayList

object CountryUtils {

    private var countries: ArrayList<Country>? = null
    private val timeZoneAndCountryISOs: Map<String, List<String>>? =
        null

    fun getFlagDrawableResId(country: Country): Int {
        return when (country.iso) {
            "ly" -> R.drawable.flag_libya
            "tn" -> R.drawable.flag_tunisia
            "tr" -> R.drawable.flag_turkey
            else -> R.drawable.flag_transparent
        }
    }

    /**
     * Get all countries
     *
     * @param context caller context
     * @return List of Country
     */
    fun getAllCountries(context: Context): ArrayList<Country>? {
        if (countries != null)
            return countries
        countries = ArrayList()


        countries!!.add(
            Country(
                context.getString(R.string.country_libya_code),
                context.getString(R.string.country_libya_number),
                context.getString(R.string.country_libya_name)
            )
        )

        countries!!.add(
            Country(
                context.getString(R.string.country_tunisia_code),
                context.getString(R.string.country_tunisia_number),
                context.getString(R.string.country_tunisia_name)
            )
        )

        countries!!.add(
            Country(
                context.getString(R.string.country_turkey_code),
                context.getString(R.string.country_turkey_number),
                context.getString(R.string.country_turkey_name)
            )
        )

        Collections.sort(countries, kotlin.Comparator { o1: Country, o2: Country ->
            o1.name.compareTo(o2.name, true)
        })

        return countries
    }


    fun getByNumber(
        context: Context,
        preferredCountries: List<Country>?,
        fullNumber: String
    ): Country? {
        if (fullNumber.isEmpty()) return null
        val firstDigit: Int = if (fullNumber[0] == '+') {
            1
        } else {
            0
        }
        var country: Country?
        for (i in firstDigit until firstDigit + 4) {
            val code = fullNumber.substring(firstDigit, i)
            country = getByCode(context, preferredCountries, code)
            if (country != null) return country
        }
        return null
    }


    fun getByCode(
        context: Context,
        preferredCountries: List<Country>?,
        code: Int
    ): Country? {
        return getByCode(context, preferredCountries, code.toString() + "")
    }

    private fun getByCode(
        context: Context,
        preferredCountries: List<Country>?,
        code: String
    ): Country? { //check in preferred countries first
        if (preferredCountries != null && !preferredCountries.isEmpty()) {
            for (country in preferredCountries) {
                if (country.phoneCode == code) {
                    return country
                }
            }
        }
        for (country in getAllCountries(context)!!) {
            if (country.phoneCode == code) {
                return country
            }
        }
        return null
    }

    fun getCountryByName(query: String?): List<Country> {
        val arrCountry = ArrayList<Country>()
        for (item in countries!!) {
            if (item.name.contains(query!!)) arrCountry.add(item)
        }
        return arrCountry
    }

}