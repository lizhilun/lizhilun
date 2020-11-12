package com.lizl.passwordbox.mvvm.activity

import android.os.Bundle
import android.os.SystemClock
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.passwordbox.R
import com.lizl.passwordbox.config.AppConfig
import com.lizl.passwordbox.config.constant.ConfigConstant
import com.lizl.passwordbox.mvvm.base.BaseActivity
import com.lizl.passwordbox.mvvm.base.BaseFragment
import com.lizl.passwordbox.mvvm.fragment.LockPasswordFragment
import com.lizl.passwordbox.util.Constant
import java.io.Serializable

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

        if (AppConfig.isAppLockPasswordOn() && !AppConfig.getAppLockPassword().isBlank()
            && SystemClock.elapsedRealtime() - lastAppStopTime >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            turnToActivity(LockActivity::class.java)
        }
        lastAppStopTime = Long.MAX_VALUE
    }

    override fun onStop()
    {
        super.onStop()

        if (ActivityUtils.getActivityList().size == 1) lastAppStopTime = SystemClock.elapsedRealtime()
    }

    private fun turnToFragment(fragmentId: Int, vararg extraList: Any)
    {
        val options = NavOptions.Builder().setEnterAnim(R.anim.slide_right_in).setExitAnim(R.anim.slide_left_out).setPopEnterAnim(R.anim.slide_left_in)
            .setPopExitAnim(R.anim.slide_right_out).build()

        val bundle = Bundle()
        extraList.forEach {
            when (it)
            {
                is Int          -> bundle.putInt(Constant.BUNDLE_DATA_INT, it)
                is Serializable -> bundle.putSerializable(Constant.BUNDLE_DATA_SERIALIZABLE, it)
            }
        }
        Navigation.findNavController(this, R.id.fragment_container).navigate(fragmentId, bundle, options)
    }

    override fun onBackPressed()
    {
        val topFragment = getTopFragment() ?: return
        if (topFragment.onBackPressed())
        {
            return
        }
        if (!Navigation.findNavController(this, R.id.fragment_container).navigateUp())
        {
            super.onBackPressed()
        }
    }

    private fun getTopFragment(): BaseFragment?
    {
        return supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.firstOrNull() as BaseFragment
    }
}
