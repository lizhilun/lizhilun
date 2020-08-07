package com.lizl.demo.passwordbox.mvvm.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.lizl.demo.passwordbox.mvvm.activity.MainActivity
import com.lizl.demo.passwordbox.util.DialogUtil

open class BaseFragment(private val layoutResId: Int) : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(layoutResId, container, false)
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
        (activity as MainActivity).turnToFragment(fragmentId, extraList)
    }
}