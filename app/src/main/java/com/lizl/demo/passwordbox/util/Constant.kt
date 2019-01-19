package com.lizl.demo.passwordbox.util

/**
 * 常数
 */
class Constant
{
    companion object StaticParams
    {
        const val FRAGMENT_SHOW_NO_DIRECTION = -1 //Fragment显示的方向：无方向
        const val FRAGMENT_SHOW_DIRECTION_LEFT = 0 //Fragment显示的方向：从左边出现
        const val FRAGMENT_SHOW_DIRECTION_RIGHT = 1 //Fragment显示的方向：从右边出现
        const val FRAGMENT_SHOW_DIRECTION_TOP = 2 //Fragment显示的方向：从顶部出现
        const val FRAGMENT_SHOW_DIRECTION_BOTTOM = 3 //Fragment显示的方向：从底部出现

        const val BUNDLE_DATA = "BUNDLE_DATA" //Fragment间传递数据的Bundle key

        const val LOCK_PASSWORD_FRAGMENT_TYPE_SET_PASSWORD = 1 //保护密码设置界面类型：设置密码
        const val LOCK_PASSWORD_FRAGMENT_TYPE_FIRST_SET_PASSWORD = 2  //保护密码设置界面类型：应用第一次启动时设置密码
        const val LOCK_PASSWORD_FRAGMENT_TYPE_MODIFY_PASSWORD = 3 //保护密码设置界面类型：修改密码

        const val APP_FINGERPRINT_STATUS_NOT_DETECT = -1 // 指纹识别状态：没有检测设备
        const val APP_FINGERPRINT_STATUS_NOT_SUPPORT = 0 // 指纹识别状态：设备不支持
        const val APP_FINGERPRINT_STATUS_SUPPORT = 1 // 指纹识别状态：设备支持

        const val DATA_RESTORE_FAILED_WRONG_PASSWORD = "DATA_RESTORE_FAILED_WRONG_PASSWORD" //数据还原失败：密码不对
    }
}