package com.lizl.demo.passwordbox.util

import android.net.Uri
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.TimeUtils
import com.lizl.demo.passwordbox.config.AppConfig
import com.lizl.demo.passwordbox.db.AppDatabase
import com.lizl.demo.passwordbox.mvvm.model.AccountModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

/**
 * 备份工具类
 */
object BackupUtil
{
    private val backupFilePath = PathUtils.getExternalAppFilesPath() + "/Backup"
    const val fileSuffixName = ".iu"
    private const val autoBackupFileName = "autoBackup"

    private val backupChannel = Channel<BackupJob>()

    private var isFirstData = true

    fun init()
    {
        GlobalScope.launch {
            for (job in backupChannel)
            {
                backupData(job)
            }
        }

        //启动数据监听，用于自动备份
        GlobalScope.launch(Dispatchers.Main) {
            AccountUtil.accountLiveData.observeForever {
                if (isFirstData)
                {
                    isFirstData = false
                    return@observeForever
                }
                if (AppConfig.isAutoBackup())
                {
                    autoBackupData()
                }
            }
        }
    }

    /**
     * 自动备份
     */
    private fun autoBackupData()
    {
        GlobalScope.launch { backupChannel.send(BackupJob(autoBackupFileName) {}) }
    }

    /**
     * 备份数据
     */
    fun backupData(callback: (result: Boolean) -> Unit)
    {
        GlobalScope.launch {
            val backupFileName = TimeUtils.millis2String(System.currentTimeMillis(), "yyyyMMdd_HHmmss")
            backupChannel.send(BackupJob(backupFileName, callback))
        }
    }

    /**
     * 备份数据
     */
    private suspend fun backupData(backupJob: BackupJob)
    {
        val backupFileName = "${backupJob.backupFileName}$fileSuffixName"
        val accountList = AppDatabase.instance.getAccountDao().getAllAccount()
        val dataString = GsonUtils.toJson(accountList)
        val encryptData = EncryptUtil.encrypt(dataString, AppConfig.getAppLockPassword())
        FileUtil.writeTxtFile(encryptData, "$backupFilePath/$backupFileName")

        FileUtils.notifySystemToScan(backupFilePath)

        delay(200)

        GlobalScope.launch(Dispatchers.Main) { backupJob.callback.invoke(true) }
    }

    /**
     * 还原数据
     */
    fun restoreData(fileUri: Uri, password: String, clearAllData: Boolean, callback: (result: Boolean, failedReason: String) -> Unit)
    {
        GlobalScope.launch(Dispatchers.IO) {

            val readResult = EncryptUtil.decrypt(FileUtil.readTxtFile(fileUri), password)

            if (readResult == null)
            {
                GlobalScope.launch(Dispatchers.Main) { callback.invoke(false, Constant.DATA_RESTORE_FAILED_WRONG_PASSWORD) }
                return@launch
            }

            // 清空之前的数据
            if (clearAllData)
            {
                AppDatabase.instance.getAccountDao().deleteAll()
            }

            val accountItemList = GsonUtils.fromJson<Array<AccountModel>>(readResult, Array<AccountModel>::class.java)
            accountItemList.forEach {
                val accountModel = AppDatabase.instance.getAccountDao().search(it.description, it.account) ?: AccountModel()
                accountModel.description = it.description
                accountModel.account = it.account
                accountModel.password = it.password
                accountModel.desPinyin = it.desPinyin
                AppDatabase.instance.getAccountDao().insert(accountModel)
            }

            GlobalScope.launch(Dispatchers.Main) { callback.invoke(true, "") }
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

    class BackupJob(val backupFileName: String, val callback: (result: Boolean) -> Unit)
}