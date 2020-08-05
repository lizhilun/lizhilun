package com.lizl.demo.passwordbox.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.demo.passwordbox.util.BackupUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File

class BackupFileViewModel : ViewModel()
{
    val backupFileLiveData = MutableLiveData<MutableList<File>>()

    fun requestBackupFileList()
    {
        GlobalScope.launch {
            backupFileLiveData.postValue(BackupUtil.getBackupFileList().toMutableList())
        }
    }
}