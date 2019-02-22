package com.lizl.demo.passwordbox.util

import android.os.Environment
import android.text.TextUtils
import com.lizl.demo.passwordbox.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * 备份工具类
 */
class BackupUtil
{
    companion object
    {
        // 备份文件路径
        var backupFilePath = Environment.getExternalStorageDirectory().absolutePath + "/PasswordBoxBackup"
        // 用于分隔描述/账号/密码的分隔字符
        var infoSeparator = "-=-="
        // 备份文件后缀名
        var fileSuffixName = ".iu"

        /**
         * 备份数据
         */
        fun backupData(callback: BackupUtil.DataBackupCallback)
        {
            GlobalScope.launch(Dispatchers.IO) {
                var dataString = ""
                val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                val backupFileName = formatter.format(System.currentTimeMillis()) + fileSuffixName
                val accountList = DataUtil.getInstance().queryAll(UiApplication.instance)
                for (accountModel in accountList!!)
                {
                    dataString += accountModel.description + infoSeparator + accountModel.account + infoSeparator + accountModel.password + "\r\n"
                }
                val encryptData = EncryptUtil.encrypt(dataString, UiApplication.instance.getAppConfig().getAppLockPassword())
                FileUtil.writeTxtFile(encryptData, "$backupFilePath/$backupFileName")

                GlobalScope.launch(Dispatchers.Main) {
                    callback.onDataBackupSuccess()
                }
            }
        }

        /**
         * 还原数据
         */
        fun restoreData(filePath: String, clearAllData: Boolean, callback: BackupUtil.DataRestoreCallback)
        {
            restoreData(filePath, "", clearAllData, callback)
        }

        /**
         * 还原数据
         */
        fun restoreData(filePath: String, password: String, clearAllData: Boolean, callback: BackupUtil.DataRestoreCallback)
        {
            GlobalScope.launch(Dispatchers.IO) {
                // 如果传入的密码为空，则使用当前App保护密码进行数据解密
                val readResult: String? = if (TextUtils.isEmpty(password))
                {
                    EncryptUtil.decrypt(FileUtil.readTxtFile(filePath), UiApplication.instance.getAppConfig().getAppLockPassword())
                }
                else
                {
                    EncryptUtil.decrypt(FileUtil.readTxtFile(filePath), password)
                }

                if (readResult == null)
                {
                    GlobalScope.launch(Dispatchers.Main) {
                        callback.onDataRestoreFailed(filePath, clearAllData, Constant.DATA_RESTORE_FAILED_WRONG_PASSWORD)
                    }
                    return@launch
                }

                // 清空之前的数据
                if (clearAllData)
                {
                    DataUtil.getInstance().deleteAllData(UiApplication.instance)
                }

                val accountItemList = readResult.split("\r\n")
                for (accountItem in accountItemList)
                {
                    val accountInfo = accountItem.split(infoSeparator)
                    if (accountInfo.size < 3)
                    {
                        continue
                    }
                    var accountModel = DataUtil.getInstance().getAccountByDesAndAccount(accountInfo[0], accountInfo[1])
                    if (accountModel == null)
                    {
                        accountModel = AccountModel()
                    }
                    accountModel.description = accountInfo[0]
                    accountModel.account = accountInfo[1]
                    accountModel.password = accountInfo[2]
                    accountModel.desPinyin = PinyinUtil.getPinyin(accountInfo[0])
                    DataUtil.getInstance().saveData(UiApplication.instance, accountModel)
                }

                GlobalScope.launch(Dispatchers.Main){
                    callback.onDataRestoreSuccess()
                }
            }
        }

        /**
         * 获取备份文件列表
         *
         * @return 文件路径列表
         */
        fun getBackupFileList(): List<String>
        {
            val filePathList = mutableListOf<String>()

            val files = File(backupFilePath).listFiles() ?: return filePathList
            for (file in files)
            {
                if (file.exists() && file.isFile && file.name.endsWith(fileSuffixName))
                {
                    filePathList.add(file.absolutePath)
                }
            }

            return filePathList
        }
    }

    interface DataBackupCallback
    {
        fun onDataBackupSuccess()

        fun onDataBackupFailed(reason: String)
    }

    interface DataRestoreCallback
    {
        fun onDataRestoreSuccess()

        fun onDataRestoreFailed(failedFilePath: String, clearAllData: Boolean, reason: String)
    }
}