package com.ssas.jibli.agent.di.components


import com.ssas.jibli.agent.di.modules.NetModule
import com.ssas.jibli.agent.repo.auth.AuthRepo
import com.ssas.jibli.agent.repo.home.HomeRepo
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [NetModule::class])
interface NetComponents {
    fun inject(homeRepo: HomeRepo)
    fun inject(authRepo: AuthRepo)

}
