package com.lizl.demo.passwordbox.util

import android.app.Application
import android.content.Context
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.view.inputmethod.InputMethodManager
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.ConfigHelper
import com.lizl.demo.passwordbox.fragment.BaseFragment
import java.lang.ref.WeakReference
import java.util.*

class UiApplication : Application()
{
    val TAG = javaClass.simpleName

    private var appConfig: AppConfig? = null
    private var configHelper: ConfigHelper? = null

    init
    {
        instance = this
    }

    override fun onCreate()
    {
        super.onCreate()

        PinyinUtil.initResource()

        checkFingerprintStatus()
    }

    /**
     * 检查指纹识别状态
     */
    private fun checkFingerprintStatus()
    {
        // 只有在未检测过情况下才检测
        if (getAppConfig().getAppFingerprintStatus() != Constant.APP_FINGERPRINT_STATUS_NOT_DETECT)
        {
            return
        }

        try
        {
            val mFingerprintManager = FingerprintManagerCompat.from(instance)
            if (mFingerprintManager.isHardwareDetected)
            {
                getAppConfig().setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_SUPPORT)
            }
            else
            {
                getAppConfig().setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
            }
        }
        catch (e: ClassNotFoundException)
        {
            getAppConfig().setAppFingerprintStatus(Constant.APP_FINGERPRINT_STATUS_NOT_SUPPORT)
        }
    }

    companion object
    {
        lateinit var instance: UiApplication
        val inputMethodManager: InputMethodManager by lazy { instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
        private val mFragmentStack = Stack<WeakReference<BaseFragment>>()

        /**
         * 判断Fragment是否在栈中
         */
        fun isFragmentInStack(fragmentName: String): Boolean
        {
            for (task in mFragmentStack)
            {
                if (task.get() != null && task.get()!!.javaClass.simpleName == fragmentName)
                {
                    return true
                }
            }
            return false
        }

        /**
         * 将Fragment压入Application栈
         */
        fun pushFragmentToStack(fragment: BaseFragment)
        {
            removeFragmentFromStack(fragment)
            val fragmentWeakReference = WeakReference<BaseFragment>(fragment)
            mFragmentStack.push(fragmentWeakReference)
        }

        /**
         * 将传入的fragment从栈中移除
         */
        fun removeFragmentFromStack(fragment: BaseFragment?)
        {
            var fragmentReference: WeakReference<BaseFragment>? = null
            for (task in mFragmentStack)
            {
                if (task.get()?.javaClass?.simpleName.equals(fragment?.javaClass?.simpleName))
                {
                    fragmentReference = task
                    break
                }
            }
            mFragmentStack.remove(fragmentReference)
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

    fun getAppConfig(): AppConfig
    {
        if (appConfig == null)
        {
            appConfig = AppConfig.getInstance()
        }
        return appConfig as AppConfig
    }

    fun getConfigHelper(): ConfigHelper
    {
        if (configHelper == null)
        {
            configHelper = ConfigHelper.getDefaultConfigHelper(instance)
        }
        return configHelper as ConfigHelper
    }
}