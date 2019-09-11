package com.lizl.demo.passwordbox.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import android.view.MotionEvent
import android.widget.EditText
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.ConfigConstant
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.UiApplication
import com.lizl.demo.passwordbox.util.UiUtil

class MainActivity : AppCompatActivity()
{
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        UiApplication.instance.getAppConfig().setAppLastStopTime(0)
    }

    override fun onStart()
    {
        super.onStart()

        // 密码保护打开并且应用超时的情况
        if (UiApplication.instance.getAppConfig().isAppLockPasswordOn() && System.currentTimeMillis() - UiApplication.instance.getAppConfig().getAppLastStopTime() >= ConfigConstant.APP_TIMEOUT_PERIOD)
        {
            // 密码为空的情况进入密码设置界面
            if (TextUtils.isEmpty(UiApplication.instance.getAppConfig().getAppLockPassword()))
            {
                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
                turnToFragment(R.id.lockPasswordFragment)
            }
            // 反之进入锁定界面
            else
            {
                turnToFragment(R.id.lockFragment)
            }
        }
    }

    override fun onSupportNavigateUp() = Navigation.findNavController(this, R.id.fragment_container).navigateUp()

    override fun onStop()
    {
        super.onStop()

        UiApplication.instance.getAppConfig().setAppLastStopTime(System.currentTimeMillis())
    }

    /**
     * 点击EditText外隐藏输入法
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean
    {
        val view = currentFocus
        if (view is EditText)
        {
            val w = currentFocus
            val location = IntArray(2)
            w!!.getLocationOnScreen(location)
            val x = ev.rawX + w.left - location[0]
            val y = ev.rawY + w.top - location[1]

            if (ev.action == MotionEvent.ACTION_UP && (x < w.left || x >= w.right || y < w.top || y > w.bottom))
            {
                UiUtil.hideInputKeyboard(view)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun turnToFragment(fragmentId: Int)
    {
        val options = NavOptions.Builder().setEnterAnim(R.anim.slide_right_in).setExitAnim(R.anim.slide_left_out).setPopEnterAnim(R.anim.slide_left_in)
                .setPopExitAnim(R.anim.slide_right_out).build()
        Navigation.findNavController(this, R.id.fragment_container).navigate(fragmentId, null, options)
    }
}
