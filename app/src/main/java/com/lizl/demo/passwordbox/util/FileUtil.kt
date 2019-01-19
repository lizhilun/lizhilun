package com.lizl.demo.passwordbox.util

import android.util.Log
import java.io.*

/**
 * 文件工具类
 */
class FileUtil
{
    companion object
    {
        private var TAG = "FileUtil"

        /**
         * 写文件
         *
         * @param content 文件内容
         * @param filePath 文件绝对路径
         *
         * @throws IOException
         */
        fun writeTxtFile(content: String, filePath: String): Boolean
        {
            var flag = true
            val thisFile = File(filePath)
            try
            {
                if (!thisFile.parentFile.exists())
                {
                    thisFile.parentFile.mkdirs()
                }
                val fw = FileWriter(filePath, false)
                fw.write(content)
                fw.close()
            }
            catch (e: IOException)
            {
                flag = false
                Log.e(TAG, e.toString())
            }
            return flag
        }

        /**
         * 读TXT文件内容
         * @param filePath 文件绝对路径
         */
        @Throws(Exception::class)
        fun readTxtFile(filePath: String): String
        {
            var result = ""
            val txtFile = File(filePath)
            var fileReader: FileReader? = null
            var bufferedReader: BufferedReader? = null
            try
            {
                fileReader = FileReader(txtFile)
                bufferedReader = BufferedReader(fileReader)
                try
                {
                    var read: String? = null
                    while ({ read = bufferedReader.readLine();read }() != null)
                    {
                        result = result + read + "\r\n"
                    }
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
            finally
            {
                bufferedReader?.close()
                fileReader?.close()
            }
            return result
        }

        /**
         * 删除文件
         */
        fun deleteFile(filePath: String): Boolean
        {
            val file = File(filePath)
            if (!file.exists())
            {
                return false
            }
            return file.delete()
        }

        /**
         * 重命名文件
         */
        fun renameFile(filePath: String, newName: String): Boolean
        {
            val file = File(filePath)
            if (!file.exists())
            {
                return false
            }

            val newFilePath = filePath.replace(file.nameWithoutExtension, newName)
            file.renameTo(File(newFilePath))
            return true
        }
    }
}