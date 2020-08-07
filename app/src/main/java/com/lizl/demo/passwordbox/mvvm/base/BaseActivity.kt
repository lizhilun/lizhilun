package com.lizl.demo.passwordbox.mvvm.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.util.Constant

open class BaseActivity(private val layoutResId: Int) : AppCompatActivity()
{
    protected val TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

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