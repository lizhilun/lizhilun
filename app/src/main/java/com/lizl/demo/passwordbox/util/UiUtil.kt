package com.lizl.demo.passwordbox.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.InputFilter
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.KeyboardUtils
import com.blankj.utilcode.util.SizeUtils
import com.lizl.demo.passwordbox.R


/**
 * UI相关工具类
 */
object UiUtil
{
    private var noWrapOrSpaceFilter: InputFilter? = null

    /**
     * 复制内容到剪切板
     */
    fun copyTextToClipboard(context: Context, text: String)
    {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
        ToastUtil.showToast(R.string.notify_success_to_copy)
    }

    /**
     * 退回到桌面
     */
    fun backToLauncher() = ActivityUtils.startHomeActivity()

    /**
     * 跳转到APP详情界面（用于获取权限）
     */
    fun goToAppDetailPage() = AppUtils.launchAppDetailsSettings()

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
    fun showSoftKeyboard(view: View) = KeyboardUtils.showSoftInput(view)

    /**
     *  隐藏键盘
     */
    fun hideSoftKeyboard(view: View) = KeyboardUtils.hideSoftInput(view)

    /**
     *  dp转px
     */
    fun dpToPx(dpValue: Int) = SizeUtils.dp2px(dpValue.toFloat())
}