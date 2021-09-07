package com.ssas.jibli.agent.di.modules

import com.ssas.jibli.agent.data.prefs.PrefMain
import com.ssas.jibli.agent.MApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MApplication) {

    @Singleton
    @Provides
    fun providePrefMain(): PrefMain {
        return PrefMain(application.applicationContext)
    }
}
