package com.lizl.demo.passwordbox.util

import android.app.Application
import android.content.Context
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.view.inputmethod.InputMethodManager
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.config.ConfigHelper

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