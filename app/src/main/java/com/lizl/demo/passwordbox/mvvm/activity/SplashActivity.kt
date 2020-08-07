package com.lizl.demo.passwordbox.mvvm.activity

import com.blankj.utilcode.util.Utils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.util.ConfigUtil
import com.lizl.demo.passwordbox.mvvm.base.BaseActivity
import com.lizl.demo.passwordbox.util.BackupUtil
import com.lizl.demo.passwordbox.util.PinyinUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity(R.layout.activity_splash)
{
    override fun initView()
    {
        super.initView()

        GlobalScope.launch {
            initApp()
            GlobalScope.launch(Dispatchers.Main) {
                onInitFinish()
            }
        }
    }

    private fun initApp()
    {
        Utils.init(this)

        ConfigUtil.initConfig()

        BackupUtil.init()

        PinyinUtil.initResource()
    }

    private fun onInitFinish()
    {
        if (AppConfig.isAppLockPasswordOn() && AppConfig.getAppLockPassword().isNotBlank())
        {
            turnToActivity(LockActivity::class.java)
        }
        else
        {
            turnToActivity(MainActivity::class.java)
        }
        finish()
    }
}