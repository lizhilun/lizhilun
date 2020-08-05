package com.lizl.demo.passwordbox.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.sourceforge.pinyin4j.PinyinHelper
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType
import java.util.*

object PinyinUtil
{
    private val defaultFormat: HanyuPinyinOutputFormat = HanyuPinyinOutputFormat().apply {
        caseType = HanyuPinyinCaseType.LOWERCASE
        toneType = HanyuPinyinToneType.WITHOUT_TONE
    }

    /**
     * 初始化（jar包内部解析时需要读取文件，耗时400ms左右，但是初始化读取文件的方法不对外公开，
     *        所以起了一个线程解析一个默认的汉字让文件读取完成，后续使用时会快很多）
     */
    fun initResource()
    {
        GlobalScope.launch(Dispatchers.IO) { getPinyin("膜法") }
    }

    /**
     * 获取拼音（暂不考虑多音字的情况）
     */
    fun getPinyin(sourceStr: String): String
    {
        val result = StringBuffer()
        for (letter in sourceStr)
        {
            if (letter.toInt() > 128)
            {
                result.append(PinyinHelper.toHanyuPinyinStringArray(letter, defaultFormat)[0])
            }
            else
            {
                result.append(letter)
            }
        }

        return result.toString().toLowerCase(Locale.getDefault())
    }

    /**
     * 获取用于排序的拼音的首字母
     */
    fun getSortFirstLetter(pinyin: String) = if (Character.isLetter(pinyin[0])) pinyin[0] else '#'
}