package com.lizl.demo.passwordbox.config

object ConfigConstant
{
    // 指纹解锁是否开启
    const val IS_FINGERPRINT_LOCK_ON = "IS_FINGERPRINT_LOCK_ON"
    const val DEFAULT_IS_FINGERPRINT_LOCK_ON = false

    // 密码保护是否开启
    const val IS_APP_LOCK_PASSWORD_ON = "IS_APP_LOCK_PASSWORD_ON"
    const val DEFAULT_IS_APP_LOCK_PASSWORD_ON = true

    // APP保护密码
    const val APP_LOCK_PASSWORD = "APP_LOCK_PASSWORD"
    const val DEFAULT_APP_LOCK_PASSWORD = ""

    // APP上次停止的时间（用于判断是否达到超时锁定时长）
    const val APP_LAST_STOP_TIME = "APP_LAST_STOP_TIME"
    const val DEFAULT_APP_LAST_STOP_TIME: Long = 0

    // APP超时锁定时长（默认为30s）
    const val APP_TIMEOUT_PERIOD = 30_000

    // 自动备份
    const val IS_AUTO_BACKUP = "IS_AUTO_BACKUP"
    const val DEFAULT_IS_AUTO_BACKUP = false
}