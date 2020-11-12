package com.lizl.passwordbox.config.constant

import com.lizl.passwordbox.config.annotation.BooleanConfig
import com.lizl.passwordbox.config.annotation.StringConfig

object ConfigConstant
{
    // 指纹解锁是否开启
    @BooleanConfig(false)
    const val IS_FINGERPRINT_LOCK_ON = "IS_FINGERPRINT_LOCK_ON"

    // 密码保护是否开启
    @BooleanConfig(true)
    const val IS_APP_LOCK_PASSWORD_ON = "IS_APP_LOCK_PASSWORD_ON"

    // APP保护密码
    @StringConfig("")
    const val APP_LOCK_PASSWORD = "APP_LOCK_PASSWORD"

    // 自动备份
    @BooleanConfig(false)
    const val IS_AUTO_BACKUP = "IS_AUTO_BACKUP"

    // APP超时锁定时长（默认为30s）
    const val APP_TIMEOUT_PERIOD = 30_000

}