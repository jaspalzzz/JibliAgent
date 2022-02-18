package com.ssas.jibli.agent.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivitySimple
import com.ssas.jibli.agent.data.prefs.PrefKeys
import com.ssas.jibli.agent.ui.home.DashboardActivity
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
            if(prefMain[PrefKeys.SALES_AGENT_CODE,""].isNullOrEmpty()){
                Utils.jumpActivity(this@SplashActivity, LoginActivity::class.java)
            }else{
                Utils.jumpActivity(this@SplashActivity, DashboardActivity::class.java)
            }
            finish()
        }
    }

}