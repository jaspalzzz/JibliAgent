package com.ssas.jibli.agent.ui.auth

import android.os.Bundle
import com.ssas.jibli.agent.R
import com.ssas.jibli.agent.base.BaseActivity
import com.ssas.jibli.agent.databinding.ActivityLandingBinding
import com.ssas.jibli.agent.repo.auth.AuthVM
import com.ssas.jibli.agent.utils.Utils

class LandingActivity : BaseActivity<ActivityLandingBinding, AuthVM>() {

    override val bindingActivity: ActivityBinding
        get() = ActivityBinding(R.layout.activity_landing, AuthVM::class.java)

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initListeners()
    }

    private fun initListeners(){
        binding.loginBt.setOnClickListener {
            Utils.jumpActivity(this@LandingActivity, LoginActivity::class.java)
        }
    }

    override fun subscribeToEvents(vm: AuthVM) {

    }
}