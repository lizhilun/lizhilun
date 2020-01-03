package com.lizl.demo.passwordbox.mvp.contract

import com.lizl.demo.passwordbox.mvp.base.BasePresenter
import com.lizl.demo.passwordbox.mvp.base.BaseView
import java.io.File

class BackupFileListContract
{
    interface View : BaseView
    {
        fun showBackupFiles(fileList: List<File>)
    }

    interface Presenter : BasePresenter<View>
    {
        fun getAllBackupFiles()
    }
}