package com.lizl.demo.passwordbox.mvvm.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.constant.ConfigConstant
import com.lizl.demo.passwordbox.mvvm.activity.LockActivity
import com.lizl.demo.passwordbox.mvvm.activity.MainActivity
import com.lizl.demo.passwordbox.util.Constant

open class BaseActivity(private val layoutResId: Int) : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    companion object
    {
        var lastAppStopTime = 0L
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        // 没开密码保护的情况下进入锁屏界面返回主界面
        if (this is LockActivity && !(AppConfig.isAppLockPasswordOn() && AppConfig.getAppLockPassword().isNotBlank()))
        {
            if (ActivityUtils.getActivityList().size == 1) turnToActivity(MainActivity::class.java)
            finish()
        }

        // Activity走onCreate()将上次应用停止时间置为0，保证onStart()会走是否显示锁定界面流程
        if (ActivityUtils.getActivityList().size == 1)
        {
            lastAppStopTime = 0L
        }

        setContentView(layoutResId)

        initView()
    }

    open fun initView()
    {

    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()

        if (this is LockActivity) return

        // 密码保护打开并且应用超时的情况
        if (AppConfig.isAppLockPasswordOn() && SystemClock.elapsedRealtime() - lastAppStopTime >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            turnToActivity(LockActivity::class.java)
        }
        else
        {
            lastAppStopTime = Long.MAX_VALUE
        }
    }

    override fun onRestart()
    {
        Log.d(TAG, "onRestart")
        super.onRestart()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()

        if (!AppUtils.isAppForeground()) lastAppStopTime = SystemClock.elapsedRealtime()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    /**
     * 点击EditText外隐藏输入法
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        if (ev.action == MotionEvent.ACTION_DOWN)
        {
            if (isShouldHideKeyboard(currentFocus, ev))
            {
                KeyboardUtils.hideSoftInput(this)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun isShouldHideKeyboard(v: View?, event: MotionEvent): Boolean
    {
        if (v is EditText)
        {
            val location = IntArray(2)
            v.getLocationInWindow(location)
            val left = location[0]
            val top = location[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }

    protected fun turnToActivity(cls: Class<out Activity>)
    {
        ActivityUtils.finishActivity(cls)
        val topActivity = ActivityUtils.getTopActivity() ?: return
        val intent = Intent(topActivity, cls)

        topActivity.startActivity(intent)
    }

    fun turnToFragment(fragmentId: Int, vararg extraList: Any)
    {
        val options = NavOptions.Builder().setEnterAnim(R.anim.slide_right_in).setExitAnim(R.anim.slide_left_out).setPopEnterAnim(R.anim.slide_left_in)
            .setPopExitAnim(R.anim.slide_right_out).build()

        val bundle = Bundle()
        extraList.forEach {
            when (it)
            {
                is Int  -> bundle.putInt(Constant.BUNDLE_DATA_INT, it)
                is Long -> bundle.putLong(Constant.BUNDLE_DATA_LONG, it)
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