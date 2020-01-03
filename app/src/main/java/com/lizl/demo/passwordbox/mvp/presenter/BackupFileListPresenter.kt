package com.lizl.demo.passwordbox.mvp.presenter

import com.lizl.demo.passwordbox.mvp.contract.BackupFileListContract
import com.lizl.demo.passwordbox.util.BackupUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class BackupFileListPresenter(private var view: BackupFileListContract.View?) : BackupFileListContract.Presenter
{
    override fun getAllBackupFiles()
    {
        GlobalScope.launch {

            val fileList = BackupUtil.getBackupFileList()

            GlobalScope.launch(Dispatchers.Main) { view?.showBackupFiles(fileList) }
        }
    }

    override fun onDestroy()
    {
        view = null
    }
}