package com.lizl.demo.passwordbox.util

/**
 * 常数
 */
object Constant
{
    const val BUNDLE_DATA_INT = "BUNDLE_DATA_INT"
    const val BUNDLE_DATA_SERIALIZABLE = "BUNDLE_DATA_SERIALIZABLE"

    const val RESULT_FAILED = 0
    const val RESULT_SUCCESS = 1

    const val LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD = 1 //保护密码设置界面类型：设置密码
    const val LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD = 2  //保护密码设置界面类型：应用第一次启动时设置密码
    const val LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD = 3 //保护密码设置界面类型：修改密码

    const val DATA_RESTORE_FAILED_WRONG_PASSWORD = "DATA_RESTORE_FAILED_WRONG_PASSWORD" //数据还原失败：密码不对
}