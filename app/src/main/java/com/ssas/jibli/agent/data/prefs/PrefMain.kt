package  com.ssas.jibli.agent.data.prefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class PrefMain @Inject
constructor(context: Context): SharedPrefPrint {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PrefKeys.PREF_NAME, Context.MODE_PRIVATE)

    override fun put(key: String, value: Int) {
        sharedPreferences.edit().putInt(key,value).apply()
    }

    override fun get(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key,defaultValue)
    }

    override fun put(key: String, value: Float) {
        sharedPreferences.edit().putFloat(key,value).apply()
    }

    override fun get(key: String, defaultValue: Float): Float {
        return sharedPreferences.getFloat(key,defaultValue)
    }

    override fun put(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key,value).apply()
    }

    override fun get(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key,defaultValue)
    }

    override fun put(key: String, value: Long) {
        sharedPreferences.edit().putLong(key,value).apply()
    }

    override fun get(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key,defaultValue)
    }

    override fun put(key: String, value: String) {
        sharedPreferences.edit().putString(key,value).apply()
    }

    override fun get(key: String, defaultValue: String): String ?{
        return sharedPreferences.getString(key,defaultValue)
    }

    override fun delete(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun deleteAll() {
        sharedPreferences.edit().clear().apply()
    }
}
