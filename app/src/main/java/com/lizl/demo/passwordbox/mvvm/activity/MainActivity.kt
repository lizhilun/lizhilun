package com.lizl.demo.passwordbox.mvvm.activity

import android.os.SystemClock
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.constant.ConfigConstant
import com.lizl.demo.passwordbox.mvvm.base.BaseActivity
import com.lizl.demo.passwordbox.mvvm.fragment.LockPasswordFragment

class MainActivity : BaseActivity(R.layout.activity_main)
{
    private var lastAppStopTime = 0L

    override fun initView()
    {
        // 密码开启但是密码为空的情况进入密码设置界面
        if (AppConfig.isAppLockPasswordOn() && AppConfig.getAppLockPassword().isBlank())
        {
            turnToFragment(R.id.lockPasswordFragment, LockPasswordFragment.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        }
        lastAppStopTime = Long.MAX_VALUE
    }

    override fun onStart()
    {
        super.onStart()

        if (AppConfig.isAppLockPasswordOn() && SystemClock.elapsedRealtime() - lastAppStopTime >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            turnToActivity(LockActivity::class.java)
        }
        lastAppStopTime = Long.MAX_VALUE
    }

    override fun onStop()
    {
        super.onStop()

        lastAppStopTime = SystemClock.elapsedRealtime()
    }
}
