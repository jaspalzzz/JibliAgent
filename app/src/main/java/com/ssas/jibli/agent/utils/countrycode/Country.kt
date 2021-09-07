package com.ssas.jibli.agent.utils.countrycode

data class Country(var iso: String, var phoneCode: String, var name: String) {
	
	/**
	 * If country have query word in name or name code or phone code, this will return true.
	 */
	
	internal fun isEligibleForQuery(query: String): Boolean {
		var query = query
		query = query.toLowerCase()
		return (name.toLowerCase().contains(query)
				|| iso.toLowerCase().contains(query)
				|| phoneCode.toLowerCase().contains(query))
	}
}