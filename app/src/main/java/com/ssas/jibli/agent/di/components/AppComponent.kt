package com.ssas.jibli.agent.di.components


import com.ssas.jibli.agent.base.BaseActivitySimple
import com.ssas.jibli.agent.base.BaseFragmentSimple
import com.ssas.jibli.agent.di.modules.AppModule
import com.ssas.jibli.agent.repo.auth.AuthVM
import com.ssas.jibli.agent.repo.home.HomeVM
import com.ssas.jibli.agent.ui.auth.dialog.LanguageChangeDialog
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(baseActivitySimple: BaseActivitySimple)
    fun inject(baseFragmentSimple: BaseFragmentSimple)
    fun inject(homeVM: HomeVM)
    fun inject(authVM: AuthVM)
    fun inject(languageChangeDialog: com.ssas.jibli.agent.ui.auth.dialog.LanguageChangeDialog)
}
