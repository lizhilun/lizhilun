package com.lizl.demo.passwordbox.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.util.DialogUtil

abstract class BaseFragment : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    var fragmentHasDestroyed = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)

        fragmentHasDestroyed = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        initView()
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

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()

        fragmentHasDestroyed = true
    }

    abstract fun getLayoutResId(): Int

    abstract fun initView()

    abstract fun onBackPressed(): Boolean

    protected fun backToPreFragment()
    {
        Navigation.findNavController(view!!)
                .navigateUp()
    }

    protected fun turnToFragment(fragmentId: Int)
    {
        turnToFragment(fragmentId, null)
    }

    protected fun turnToFragment(fragmentId: Int, bundle: Bundle?)
    {
        val options = NavOptions.Builder()
                .setEnterAnim(R.anim.slide_right_in)
                .setExitAnim(R.anim.slide_left_out)
                .setPopEnterAnim(R.anim.slide_left_in)
                .setPopExitAnim(R.anim.slide_right_out)
                .build()
        Navigation.findNavController(view!!)
                .navigate(fragmentId, bundle, options)
    }
}