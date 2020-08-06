package com.lizl.demo.passwordbox.config

import com.blankj.utilcode.util.SPUtils

object AppConfig
{
    fun isFingerPrintLockOn() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON, ConfigConstant.DEFAULT_IS_FINGERPRINT_LOCK_ON)

    fun isAppLockPasswordOn() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, ConfigConstant.DEFAULT_IS_APP_LOCK_PASSWORD_ON)

    fun setAppLockPasswordOn(isOn: Boolean) = SPUtils.getInstance().put(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, isOn)

    fun getAppLockPassword(): String = SPUtils.getInstance().getString(ConfigConstant.APP_LOCK_PASSWORD, ConfigConstant.DEFAULT_APP_LOCK_PASSWORD)

    fun setAppLockPassword(password: String) = SPUtils.getInstance().put(ConfigConstant.APP_LOCK_PASSWORD, password)

    fun setAppLastStopTime(stopTime: Long) = SPUtils.getInstance().put(ConfigConstant.APP_LAST_STOP_TIME, stopTime)

    fun getAppLastStopTime() = SPUtils.getInstance().getLong(ConfigConstant.APP_LAST_STOP_TIME, ConfigConstant.DEFAULT_APP_LAST_STOP_TIME)

    fun isAutoBackup() = SPUtils.getInstance().getBoolean(ConfigConstant.IS_AUTO_BACKUP, ConfigConstant.DEFAULT_IS_AUTO_BACKUP)
}