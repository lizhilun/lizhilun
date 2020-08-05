package com.lizl.demo.passwordbox.util

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.FileProvider
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import java.io.File

/**
 * 文件工具类
 */
object FileUtil
{
    private const val TAG = "FileUtil"

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

    /**
     * 获取文件uri
     */
    fun getFileUri(file: File): Uri
    {
        return FileProvider.getUriForFile(ActivityUtils.getTopActivity(), "com.lizl.demo.passwordbox.fileprovider", file);
    }

    /**
     * 分享所有类型的文件
     */
    fun shareAllTypeFile(file: File)
    {
        try
        {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_STREAM, getFileUri(file))
            shareIntent.type = "*/*"; //此处可发送多种文件
            ActivityUtils.getTopActivity().startActivity(Intent.createChooser(shareIntent, ""))
        }
        catch (e: Exception)
        {
            Log.e(TAG, "shareAllTypeFile error:", e)
        }
    }
}