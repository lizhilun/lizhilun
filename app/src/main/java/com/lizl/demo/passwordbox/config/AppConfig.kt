package com.lizl.demo.passwordbox.config

import com.blankj.utilcode.util.SPUtils
import com.lizl.demo.passwordbox.util.Constant

object AppConfig
{
    fun isFingerPrintLockOn(): Boolean
    {
        return SPUtils.getInstance().getBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON)
    }

    fun setFingerPrintLock(isOn: Boolean)
    {
        SPUtils.getInstance().put(ConfigConstant.IS_FINGERPRINT_LOCK_ON, isOn)
    }

    fun isAppLockPasswordOn(): Boolean
    {
        return SPUtils.getInstance().getBoolean(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_PASSWORD_ON)
    }

    fun setAppLockPasswordOn(isOn: Boolean)
    {
        SPUtils.getInstance().put(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, isOn)
    }

    fun getAppLockPassword(): String
    {
        return SPUtils.getInstance().getString(ConfigConstant.APP_LOCK_PASSWORD, ConfigConstant.DEFAULT_APP_LOCK_PASSWORD)!!
    }

    fun setAppLockPassword(password: String)
    {
        SPUtils.getInstance().put(ConfigConstant.APP_LOCK_PASSWORD, password)
    }

    fun getAppFingerprintStatus(): Int
    {
        return SPUtils.getInstance().getInt(ConfigConstant.APP_FINGERPRINT_STATUS, ConfigConstant.DEFAULT_APP_FINGERPRINT_STATUS)
    }

    fun setAppFingerprintStatus(status: Int)
    {
        SPUtils.getInstance().put(ConfigConstant.APP_FINGERPRINT_STATUS, status)
    }

    fun isAppFingerprintSupport(): Boolean
    {
        return getAppFingerprintStatus() == Constant.APP_FINGERPRINT_STATUS_SUPPORT
    }

    fun setAppLastStopTime(stopTime: Long)
    {
        SPUtils.getInstance().put(ConfigConstant.APP_LAST_STOP_TIME, stopTime)
    }

    fun getAppLastStopTime(): Long
    {
        return SPUtils.getInstance().getLong(ConfigConstant.APP_LAST_STOP_TIME, ConfigConstant.DEFAULT_APP_LAST_STOP_TIME)
    }
}