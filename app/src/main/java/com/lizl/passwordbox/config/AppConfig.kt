package com.lizl.passwordbox.config

import com.lizl.passwordbox.config.constant.ConfigConstant
import com.lizl.passwordbox.config.util.ConfigUtil

object AppConfig
{
    fun isFingerPrintLockOn() = ConfigUtil.getBoolean(ConfigConstant.IS_FINGERPRINT_LOCK_ON)

    fun isAppLockPasswordOn() = ConfigUtil.getBoolean(ConfigConstant.IS_APP_LOCK_PASSWORD_ON)

    fun setAppLockPasswordOn(isOn: Boolean) = ConfigUtil.set(ConfigConstant.IS_APP_LOCK_PASSWORD_ON, isOn)

    fun getAppLockPassword(): String = ConfigUtil.getString(ConfigConstant.APP_LOCK_PASSWORD)

    fun setAppLockPassword(password: String) = ConfigUtil.set(ConfigConstant.APP_LOCK_PASSWORD, password)

    fun isAutoBackup() = ConfigUtil.getBoolean(ConfigConstant.IS_AUTO_BACKUP)
}