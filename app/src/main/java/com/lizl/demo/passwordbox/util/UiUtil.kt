package com.lizl.demo.passwordbox.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.InputFilter
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import com.blankj.utilcode.util.KeyboardUtils
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.UiApplication


/**
 * UI相关工具类
 */
class UiUtil
{
    companion object
    {
        private var noWrapOrSpaceFilter: InputFilter? = null

        /**
         * 复制内容到剪切板
         */
        fun copyTextToClipboard(context: Context, text: String)
        {
            val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
            if (clipboardManager.hasPrimaryClip())
            {
                clipboardManager.primaryClip!!.getItemAt(0).text
            }
            ToastUtil.showToast(R.string.notify_success_to_copy)
        }

        /**
         * 隐藏输入法
         */
        fun hideInputKeyboard(view: View)
        {
            val manager = UiApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (manager.isActive)
            {
                manager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }

        /**
         * 退回到桌面
         */
        fun backToLauncher()
        {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addCategory(Intent.CATEGORY_HOME)
            UiApplication.instance.startActivity(intent)
        }

        /**
         * 跳转到APP详情界面（用于获取权限）
         */
        fun goToAppDetailPage()
        {
            val intent = Intent()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            intent.data = Uri.fromParts("package", UiApplication.instance.packageName, null)
            UiApplication.instance.startActivity(intent)
        }

        /**
         * 去除TextView默认换行样式，自定义换行
         */
        fun clearTextViewAutoWrap(tv: TextView)
        {
            tv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener
            {
                override fun onGlobalLayout()
                {
                    tv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    val newText = autoSplitText(tv)
                    tv.text = newText
                }
            })
        }

        /**
         * 自定义TextView换行
         */
        private fun autoSplitText(tv: TextView): String
        {
            val rawText = tv.text.toString() //原始文本
            val tvPaint = tv.paint //paint，包含字体等信息
            val tvWidth = (tv.width - tv.paddingLeft - tv.paddingRight).toFloat() //控件可用宽度

            //将原始文本按行拆分
            val rawTextLines = rawText.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val sb = StringBuilder()
            for (rawTextLine in rawTextLines)
            {
                if (tvPaint.measureText(rawTextLine) <= tvWidth)
                {
                    //如果整行宽度在控件可用宽度之内，就不处理了
                    sb.append(rawTextLine)
                }
                else
                {
                    //如果整行宽度超过控件可用宽度，则按字符测量，在超过可用宽度的前一个字符处手动换行
                    var lineWidth = 0f
                    var cnt = 0
                    while (cnt != rawTextLine.length)
                    {
                        val ch = rawTextLine[cnt]
                        lineWidth += tvPaint.measureText(ch.toString())
                        if (lineWidth <= tvWidth)
                        {
                            sb.append(ch)
                        }
                        else
                        {
                            sb.append("\n")
                            lineWidth = 0f
                            --cnt
                        }
                        ++cnt
                    }
                }
                sb.append("\n")
            }

            //把结尾多余的\n去掉
            if (!rawText.endsWith("\n"))
            {
                sb.deleteCharAt(sb.length - 1)
            }
            return sb.toString()
        }

        /**
         * 获取限制空格和换行的inputFilter
         */
        fun getNoWrapOrSpaceFilter(): InputFilter
        {
            if (noWrapOrSpaceFilter == null)
            {
                noWrapOrSpaceFilter = InputFilter { source, start, end, dest, dstart, dend ->
                    if (source == " " || source.toString().contentEquals("\n"))
                    {
                        ""
                    }
                    else
                    {
                        null
                    }
                }
            }
            return noWrapOrSpaceFilter!!
        }

        /**
         *  显示键盘
         */
        fun showSoftKeyboard(view: View)
        {
            KeyboardUtils.showSoftInput(view)
        }

        /**
         *  隐藏键盘
         */
        fun hideSoftKeyboard(view: View)
        {
            KeyboardUtils.hideSoftInput(view)
        }

        /**
         *  dp转px
         */
        fun dpToPx(dpValue: Int): Int
        {
            val scale = UiApplication.instance.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }
}