package com.lizl.demo.passwordbox.mvp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.DialogUtil
import java.io.Serializable

abstract class BaseFragment<T : BasePresenter<*>> : Fragment()
{
    protected var TAG = this.javaClass.simpleName

    protected lateinit var presenter: T

    override fun onCreate(savedInstanceState: Bundle?)
    {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(getLayoutResId(), container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)

        presenter = initPresenter()

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

    override fun onDestroyView()
    {
        Log.d(TAG, "onDestroyView")

        presenter.onDestroy()

        super.onDestroyView()
    }

    override fun onDestroy()
    {
        Log.d(TAG, "onDestroy")
        super.onDestroy()
    }

    abstract fun getLayoutResId(): Int

    abstract fun initPresenter(): T

    abstract fun initView()

    abstract fun onBackPressed(): Boolean

    protected fun backToPreFragment()
    {
        try
        {
            Navigation.findNavController(checkNotNull(view)).navigateUp()
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
        }
    }

    protected fun turnToFragment(fragmentId: Int, vararg extraList: Any)
    {
        try
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

            Navigation.findNavController(checkNotNull(view)).navigate(fragmentId, bundle, options)
        }
        catch (e: Exception)
        {
            Log.e(TAG, e.toString())
        }
    }
}