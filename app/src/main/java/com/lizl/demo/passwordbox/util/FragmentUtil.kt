package com.lizl.demo.passwordbox.util

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.fragment.AccountListFragment
import com.lizl.demo.passwordbox.fragment.BaseFragment
import java.lang.ref.WeakReference
import java.util.*

class FragmentUtil
{
    companion object
    {
        private val TAG = "FragmentUtil"
        private val mFragmentStack = Stack<WeakReference<BaseFragment>>()

        /**
         * 跳转Fragment(不传递数据)
         */
        fun turnToFragment(activity: AppCompatActivity, fragment: BaseFragment)
        {
            Log.d(TAG, "turnToFragment")
            turnToFragment(activity, fragment, null)
        }

        /**
         * 跳转Fragment(带传递数据)
         */
        fun turnToFragment(activity: AppCompatActivity, fragment: BaseFragment, bundle: Bundle?)
        {
            Log.d(TAG, "turnToFragment")
            if (bundle == null || bundle.isEmpty)
            {
                fragment.arguments = null
            }
            else
            {
                fragment.arguments?.clear()
                fragment.arguments?.putAll(bundle)
                fragment.arguments = bundle
            }
            pushFragmentToStack(fragment)
            showFragmentFromRight(activity, fragment)
        }

        /**
         * 显示栈顶Fragment
         */
        fun showTopFragment(activity: AppCompatActivity)
        {
            Log.d(TAG, "showTopFragment")
            val topFragment = getTopFragment()
            if (topFragment == null)
            {
                turnToFragment(activity, AccountListFragment())
                return
            }

            if (topFragment.fragmentHasDestroyed)
            {
                turnToFragment(activity, topFragment)
            }
        }

        /**
         * 回退到上一个Fragment
         */
        fun backToPreFragment(activity: AppCompatActivity)
        {
            Log.d(TAG, "backToPreFragment")
            removeFragmentFromStack(getTopFragment())

            val preFragment = getTopFragment()
            showFragmentFromLeft(activity, preFragment ?: AccountListFragment())
        }

        /**
         * 从右边显示Fragment
         */
        private fun showFragmentFromRight(activity: AppCompatActivity, fragment: BaseFragment)
        {
            val annotation: IntArray = UiUtil.getFragmentTransactionAnnotation(Constant.FRAGMENT_SHOW_DIRECTION_RIGHT)
            activity.supportFragmentManager.beginTransaction().setCustomAnimations(
                    annotation[0], annotation[1], annotation[2], annotation[3]
            ).replace(R.id.container, fragment).commit()
            activity.supportFragmentManager.beginTransaction().show(fragment).commit()
        }

        /**
         * 从左边显示Fragment
         */
        private fun showFragmentFromLeft(activity: AppCompatActivity, fragment: BaseFragment)
        {
            val annotation: IntArray = UiUtil.getFragmentTransactionAnnotation(Constant.FRAGMENT_SHOW_DIRECTION_LEFT)
            activity.supportFragmentManager.beginTransaction().setCustomAnimations(
                    annotation[0], annotation[1], annotation[2], annotation[3]
            ).replace(R.id.container, fragment).commit()
            activity.supportFragmentManager.beginTransaction().show(fragment).commit()
        }

        /**
         * 判断Fragment是否在栈中
         */
        fun isFragmentInStack(fragmentName: String): Boolean
        {
            return mFragmentStack.find { it.get() != null && it.get()!!.javaClass.simpleName == fragmentName } != null
        }

        /**
         * 将Fragment压入Application栈
         */
        private fun pushFragmentToStack(fragment: BaseFragment)
        {
            removeFragmentFromStack(fragment)
            val fragmentWeakReference = WeakReference<BaseFragment>(fragment)
            mFragmentStack.push(fragmentWeakReference)
        }

        /**
         * 将传入的fragment从栈中移除
         */
        private fun removeFragmentFromStack(fragment: BaseFragment?)
        {
            mFragmentStack.remove(mFragmentStack.find { it.get()?.javaClass?.simpleName.equals(fragment?.javaClass?.simpleName) })
        }

        /**
         * 获取栈顶Fragment
         */
        fun getTopFragment(): BaseFragment?
        {
            if (mFragmentStack.size < 1)
            {
                return null
            }
            return mFragmentStack.peek().get()
        }
    }
}