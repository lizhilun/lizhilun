package com.lizl.demo.passwordbox.config

import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.UiApplication

class AppConfig
{
    private var configHelper: ConfigHelper? = null

    init
    {
        configHelper = UiApplication.instance.getConfigHelper()
    }

    companion object
    {
        private var instance: AppConfig? = null

        @Synchronized
        fun getInstance(): AppConfig
        {
            if (instance == null)
            {
                instance = AppConfig()
            }
            return instance as AppConfig
        }
    }

    fun isFingerPrintLockOn(): Boolean
    {
        return configHelper!!.getBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON)
    }

    fun setFingerPrintLock(isOn: Boolean)
    {
        configHelper!!.putBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON, isOn)
    }

    fun isAppLockPasswordOn(): Boolean
    {
        return configHelper!!.getBoolean(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_PASSWORD_ON)
    }

    fun setAppLockPasswordOn(isOn: Boolean)
    {
        configHelper!!.putBoolean(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, isOn)
    }

    fun getAppLockPassword(): String
    {
        return configHelper!!.getString(ConfigConstant.APP_LOCK_PASSWORD, ConfigConstant.DEFAULT_APP_LOCK_PASSWORD)!!
    }

    fun setAppLockPassword(password: String)
    {
        configHelper!!.putString(ConfigConstant.APP_LOCK_PASSWORD, password)
    }

    fun getAppFingerprintStatus(): Int
    {
        return configHelper!!.getInt(ConfigConstant.APP_FINGERPRINT_STATUS, ConfigConstant.DEFAULT_APP_FINGERPRINT_STATUS)
    }

    fun setAppFingerprintStatus(status: Int)
    {
        configHelper!!.putInt(ConfigConstant.APP_FINGERPRINT_STATUS, status)
    }

    fun isAppFingerprintSupport(): Boolean
    {
        return getAppFingerprintStatus() == Constant.APP_FINGERPRINT_STATUS_SUPPORT
    }

    fun setAppLastStopTime(stopTime: Long)
    {
        configHelper!!.putLong(ConfigConstant.APP_LAST_STOP_TIME, stopTime)
    }

    fun getAppLastStopTime(): Long
    {
        return configHelper!!.getLong(ConfigConstant.APP_LAST_STOP_TIME, ConfigConstant.DEFAULT_APP_LAST_STOP_TIME)
    }
}