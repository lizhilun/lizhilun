package com.lizl.demo.passwordbox.util

import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils

/**
 * 文件工具类
 */
object FileUtil
{
    /**
     * 写文件
     *
     * @param content 文件内容
     * @param filePath 文件绝对路径
     */
    fun writeTxtFile(content: String, filePath: String) = FileIOUtils.writeFileFromString(filePath, content)

    /**
     * 读TXT文件内容
     * @param filePath 文件绝对路径
     */
    @Throws(Exception::class)
    fun readTxtFile(filePath: String) = FileIOUtils.readFile2String(filePath)

    /**
     * 删除文件
     */
    fun deleteFile(filePath: String) = FileUtils.deleteFile(filePath)

    /**
     * 重命名文件
     */
    fun renameFile(filePath: String, newName: String) = FileUtils.rename(filePath, newName)
}