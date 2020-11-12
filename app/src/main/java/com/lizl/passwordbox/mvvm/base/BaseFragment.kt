package com.lizl.passwordbox.mvvm.base

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.lizl.passwordbox.R
import com.lizl.passwordbox.util.Constant
import com.lizl.passwordbox.util.DialogUtil
import java.io.Serializable

open class BaseFragment(layoutResId: Int) : Fragment(layoutResId)
{
    protected var TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        initView()
        initData()
        initListener()
    }

    override fun onStart()
    {
        Log.d(TAG, "onStart")
        super.onStart()
    }

    override fun onResume()
    {
        Log.d(TAG, "onResume")
        super.onResume()
    }

    override fun onPause()
    {
        Log.d(TAG, "onPause")
        super.onPause()

        DialogUtil.dismissDialog()
    }

    override fun onStop()
    {
        Log.d(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroyView()
    {
        Log.d(TAG, "onDestroyView")

        super.onDestroyView()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    open fun initView()
    {

    }

    open fun initData()
    {

    }

    open fun initListener()
    {

    }

    open fun onBackPressed() = false

    protected fun backToPreFragment()
    {
        try
        {
            Navigation.findNavController(requireView()).navigateUp()
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
        }
    }

    protected fun turnToFragment(fragmentId: Int, vararg extraList: Any)
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
        Navigation.findNavController(requireView()).navigate(fragmentId, bundle, options)
    }
}