package com.lizl.demo.passwordbox.util

import android.text.TextUtils
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Toast工具类
 */
class ToastUtil
{
    companion object
    {
        fun showToast(textResId: Int)
        {
            val toastText = UiApplication.getInstance().getString(textResId)
            showToast(toastText)
        }

        fun showToast(toastText: String)
        {
            if (TextUtils.isEmpty(toastText))
            {
                return
            }

            GlobalScope.launch(Dispatchers.Main) {
                Toast.makeText(UiApplication.getInstance(), toastText, Toast.LENGTH_SHORT).show()
            }
        }

    }
}