package com.lizl.demo.passwordbox.util

import android.os.Environment
import android.text.TextUtils
import com.blankj.utilcode.util.GsonUtils
import com.lizl.demo.passwordbox.config.AppConfig
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
object BackupUtil
{
    // 备份文件路径
    var backupFilePath = Environment.getExternalStorageDirectory().absolutePath + "/PasswordBoxBackup"
    // 备份文件后缀名
    var fileSuffixName = ".iu"

    /**
     * 备份数据
     */
    fun backupData(callback: (result: Int) -> Unit)
    {
        GlobalScope.launch(Dispatchers.IO) {

            val formatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val backupFileName = formatter.format(System.currentTimeMillis()) + fileSuffixName
            val accountList = AppDatabase.instance.getAccountDao().getAllDiary()
            val dataString = GsonUtils.toJson(accountList)
            val encryptData = EncryptUtil.encrypt(dataString, AppConfig.getAppLockPassword())
            FileUtil.writeTxtFile(encryptData, "$backupFilePath/$backupFileName")

            GlobalScope.launch(Dispatchers.Main) { callback.invoke(Constant.RESULT_SUCCESS) }
        }
    }

    /**
     * 还原数据
     */
    fun restoreData(filePath: String, clearAllData: Boolean, callback: DataRestoreCallback)
    {
        restoreData(filePath, "", clearAllData, callback)
    }

    /**
     * 还原数据
     */
    fun restoreData(filePath: String, password: String, clearAllData: Boolean, callback: DataRestoreCallback)
    {
        GlobalScope.launch(Dispatchers.IO) {
            // 如果传入的密码为空，则使用当前App保护密码进行数据解密
            val readResult: String? = if (TextUtils.isEmpty(password))
            {
                EncryptUtil.decrypt(FileUtil.readTxtFile(filePath), AppConfig.getAppLockPassword())
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
                AppDatabase.instance.getAccountDao().deleteAll()
            }

            val accountItemList = GsonUtils.fromJson<Array<AccountModel>>(readResult, Array<AccountModel>::class.java)
            accountItemList.forEach {
                var accountModel = AppDatabase.instance.getAccountDao().search(it.description, it.account)
                if (accountModel == null)
                {
                    accountModel = AccountModel()
                }
                accountModel.description = it.description
                accountModel.account = it.account
                accountModel.password = it.password
                accountModel.desPinyin = it.desPinyin
                AppDatabase.instance.getAccountDao().insert(accountModel)
            }

            GlobalScope.launch(Dispatchers.Main) {
                callback.onDataRestoreSuccess()
            }
        }
    }

    /**
     * 获取备份文件列表
     *
     * @return 文件路径列表
     */
    fun getBackupFileList(): List<File>
    {
        return File(backupFilePath).listFiles()?.filter { it.exists() && it.isFile && it.name.endsWith(fileSuffixName) } ?: emptyList()
    }

    interface DataRestoreCallback
    {
        fun onDataRestoreSuccess()

        fun onDataRestoreFailed(failedFilePath: String, clearAllData: Boolean, reason: String)
    }
}