package com.ssas.jibli.agent

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.di.components.AppComponent
import com.ssas.jibli.agent.di.components.DaggerAppComponent
import com.ssas.jibli.agent.di.components.DaggerNetComponents
import com.ssas.jibli.agent.di.components.NetComponents
import com.ssas.jibli.agent.di.modules.AppModule
import com.ssas.jibli.agent.di.modules.NetModule
import com.ssas.jibli.agent.utils.LanguageUtils


class MApplication : Application() {
    lateinit var pref: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        instance = this
        provider = ViewModelProvider.AndroidViewModelFactory(this)

        netComponents = DaggerNetComponents.builder()
            .netModule(NetModule())
            .build()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        pref = getSharedPreferences(PrefKeys.PREF_NAME, Context.MODE_PRIVATE)
        language = pref.getString(PrefKeys.LANGUAGE, LanguageUtils.ARABIC) ?: LanguageUtils.ARABIC
    }


    companion object {
        lateinit var instance: MApplication
        lateinit var provider: ViewModelProvider.NewInstanceFactory
        lateinit var appComponent: AppComponent
        lateinit var netComponents: NetComponents
        var language = LanguageUtils.ARABIC
    }
}