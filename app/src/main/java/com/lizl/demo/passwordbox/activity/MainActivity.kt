package com.lizl.demo.passwordbox.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.MotionEvent
import android.widget.EditText
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.config.ConfigConstant
import com.lizl.demo.passwordbox.fragment.AccountListFragment
import com.lizl.demo.passwordbox.fragment.BaseFragment
import com.lizl.demo.passwordbox.fragment.LockFragment
import com.lizl.demo.passwordbox.fragment.LockPasswordFragment
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.UiApplication
import com.lizl.demo.passwordbox.util.UiUtil

class MainActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Activity走onCreate()将上次应用停止时间置为0，保证onResume()会走是否显示锁定界面流程
        UiApplication.getInstance().getAppConfig().setAppLastStopTime(0)
    }

    override fun onResume()
    {
        super.onResume()

        // 应用超时并且密码保护打开的情况
        if (System.currentTimeMillis() - UiApplication.getInstance().getAppConfig().getAppLastStopTime() >= ConfigConstant.APP_TIMEOUT_PERIOD && UiApplication.getInstance().getAppConfig().isAppLockPasswordOn())
        {
            // 密码为空的情况进入密码设置界面
            if (TextUtils.isEmpty(UiApplication.getInstance().getAppConfig().getAppLockPassword()))
            {
                val bundle = Bundle()
                bundle.putInt(Constant.BUNDLE_DATA, Constant.LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD)
                turnToFragment(LockPasswordFragment(), bundle)
            }
            // 反之进入锁定界面
            else
            {
                turnToFragment(LockFragment())
            }
        }
        // 反之直接显示栈顶Fragment
        else
        {
            showTopFragment()
        }
    }

    override fun onStop()
    {
        super.onStop()

        UiApplication.getInstance().getAppConfig().setAppLastStopTime(System.currentTimeMillis())
    }

    /**
     * 显示栈顶Fragment
     */
    private fun showTopFragment()
    {
        if (UiApplication.getTopFragment() != null)
        {
            if (UiApplication.getTopFragment()!!.fragmentHasDestroyed)
            {
                turnToFragment(UiApplication.getTopFragment()!!)
            }
        }
        else
        {
            turnToFragment(AccountListFragment())
        }
    }

    /**
     * 跳转Fragment(不传递数据)
     */
    fun turnToFragment(fragment: BaseFragment)
    {
        turnToFragment(fragment, null)
    }

    /**
     * 跳转Fragment(带传递数据)
     */
    fun turnToFragment(fragment: BaseFragment, bundle: Bundle?)
    {
        if (bundle == null || bundle.isEmpty)
        {
            if (fragment.arguments != null)
            {
                fragment.arguments = null
            }
        }
        else
        {
            if (fragment.arguments != null)
            {
                fragment.arguments!!.clear()
                fragment.arguments!!.putAll(bundle)
            }
            else
            {
                fragment.arguments = bundle
            }
        }
        UiApplication.pushFragmentToStack(fragment)
        showFragmentFromRight(fragment)
    }

    /**
     * 回退到上一个Fragment
     */
    fun backToPreFragment()
    {
        UiApplication.removeFragmentFromStack(UiApplication.getTopFragment())

        val preFragment = UiApplication.getTopFragment()
        if (preFragment == null)
        {
            showFragmentFromLeft(AccountListFragment())
        }
        else
        {
            showFragmentFromLeft(preFragment)
        }
    }

    /**
     * 从右边显示Fragment
     */
    private fun showFragmentFromRight(fragment: BaseFragment)
    {
        val annotation: IntArray = UiUtil.getFragmentTransactionAnnotation(Constant.FRAGMENT_SHOW_DIRECTION_RIGHT)
        supportFragmentManager.beginTransaction().setCustomAnimations(
                annotation[0], annotation[1], annotation[2], annotation[3]
        ).replace(R.id.container, fragment).commit()
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }

    /**
     * 从左边显示Fragment
     */
    private fun showFragmentFromLeft(fragment: BaseFragment)
    {
        val annotation: IntArray = UiUtil.getFragmentTransactionAnnotation(Constant.FRAGMENT_SHOW_DIRECTION_LEFT)
        supportFragmentManager.beginTransaction().setCustomAnimations(
                annotation[0], annotation[1], annotation[2], annotation[3]
        ).replace(R.id.container, fragment).commit()
        supportFragmentManager.beginTransaction().show(fragment).commit()
    }

    override fun onBackPressed()
    {
        val topFragment = UiApplication.getTopFragment()
        val result = topFragment?.onBackPressed()
        if (result == null || !result)
        {
            super.onBackPressed()
        }
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
}
