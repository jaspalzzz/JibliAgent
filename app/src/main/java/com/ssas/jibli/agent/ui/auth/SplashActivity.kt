package com.ssas.jibli.agent.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.lifecycleScope
import com.ssas.jibli.agent.MApplication
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivitySimple
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.ui.home.DashboardActivity
import com.ssas.jibli.agent.utils.LanguageUtils
import com.ssas.jibli.agent.utils.Utils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivitySimple() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startSplash()
    }

    private fun startSplash() {
        lifecycleScope.launch {
            delay(1500)
            val language = prefMain[PrefKeys.LANGUAGE, LanguageUtils.ARABIC]
            MApplication.language = language ?: LanguageUtils.ARABIC
            LanguageUtils.setLanguage(this@SplashActivity, MApplication.language).run {
                if (prefMain[PrefKeys.SALES_AGENT_CODE, ""].isNullOrEmpty()) {
                    Utils.jumpActivity(this@SplashActivity, LandingActivity::class.java)
                } else {
                    Utils.jumpActivity(this@SplashActivity, DashboardActivity::class.java)
                }
            }
            finish()
        }
    }

}