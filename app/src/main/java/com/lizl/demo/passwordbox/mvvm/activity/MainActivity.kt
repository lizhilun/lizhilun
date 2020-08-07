package com.lizl.demo.passwordbox.mvvm.activity

import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.mvvm.base.BaseActivity
import com.lizl.demo.passwordbox.mvvm.fragment.LockPasswordFragment

class MainActivity : BaseActivity(R.layout.activity_main)
{
    override fun initView()
    {
        // 密码开启但是密码为空的情况进入密码设置界面
        if (AppConfig.isAppLockPasswordOn() && AppConfig.getAppLockPassword().isBlank())
        {
            turnToFragment(R.id.lockPasswordFragment, LockPasswordFragment.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
        }
    }
}
