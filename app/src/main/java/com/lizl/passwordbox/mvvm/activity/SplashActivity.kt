package com.lizl.passwordbox.mvvm.activity

import android.util.Log
import com.blankj.utilcode.util.Utils
import com.lizl.passwordbox.R
import com.lizl.passwordbox.config.AppConfig
import com.lizl.passwordbox.config.util.ConfigUtil
import com.lizl.passwordbox.mvvm.base.BaseActivity
import com.lizl.passwordbox.util.BackupUtil
import com.lizl.passwordbox.util.PinyinUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity(R.layout.activity_splash)
{
    companion object
    {
        var isInitFinish = false
    }

    override fun initView()
    {
        super.initView()

        GlobalScope.launch {
            if (!isInitFinish)
            {
                initApp()
                isInitFinish = true
            }
            GlobalScope.launch(Dispatchers.Main) {
                onInitFinish()
            }
        }
    }

    private fun initApp()
    {
        Log.d(TAG, "initApp() called")

        Utils.init(this)

        ConfigUtil.initConfig()

        BackupUtil.init()

        PinyinUtil.initResource()
    }

    private fun onInitFinish()
    {
        Log.d(TAG, "onInitFinish() called")

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