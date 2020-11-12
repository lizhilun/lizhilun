package com.lizl.passwordbox.util

import com.blankj.utilcode.util.ToastUtils

/**
 * Toast工具类
 */
object ToastUtil
{
    fun showToast(textResId: Int)
    {
        ToastUtils.showShort(textResId)
    }

    fun showToast(toastText: String)
    {
        ToastUtils.showShort(toastText)
    }
}