package com.lizl.demo.passwordbox.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.BackupFileListAdapter
import com.lizl.demo.passwordbox.customview.CustomTitleBar
import com.lizl.demo.passwordbox.customview.dialog.DialogInput
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationConfirm
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class BackupFileListFragment : BaseFragment(), BackupFileListAdapter.OnBackFileItemClickListener
{

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_backup_file_list
    }

    override fun initView()
    {
        ctb_title.setOnBackBtnClickListener(object : CustomTitleBar.OnBackBtnClickListener
        {
            override fun onBackBtnClick()
            {
                backToPreFragment()
            }
        })
    }

    override fun onResume()
    {
        super.onResume()

        //TODO:延时300ms加载数据，防止界面切换卡顿
        GlobalScope.launch(Dispatchers.Main) {
            delay(300)
            getData()
        }
    }

    private fun getData()
    {
        val backupFileList = BackupUtil.getBackupFileList()
        val backupFileListAdapter = BackupFileListAdapter(backupFileList, this)
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter
    }

    override fun onBackupFileItemClick(file: File)
    {
        val operationList = mutableListOf<OperationItem>()

        // 覆盖导入备份
        operationList.add(OperationItem(getString(R.string.import_backup_file_overlay), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_overlay), getString(R.string.notify_restore_data_overlay), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        BackupUtil.restoreData(file.absolutePath, true, DataRestoreCallback())
                    }
                })
            }
        }))

        // 合并导入备份
        operationList.add(OperationItem(getString(R.string.import_backup_file_merge), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_merge), getString(R.string.notify_restore_data_merge), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        BackupUtil.restoreData(file.absolutePath, false, DataRestoreCallback())
                    }
                })
            }
        }))

        // 删除备份文件
        operationList.add(OperationItem(getString(R.string.delete_backup_file), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.delete_backup_file), getString(R.string.notify_delete_backup_file), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        if (FileUtil.deleteFile(file.absolutePath))
                        {
                            getData()
                        }
                    }
                })
            }
        }))

        // 重命名备份文件
        operationList.add(OperationItem(getString(R.string.rename_backup_file), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.rename_backup_file), getString(R.string.hint_rename_backup_file), object : DialogInput.InputCompletedCallback
                {
                    override fun onInputCompleted(inputValue: String)
                    {
                        if (FileUtil.renameFile(file.absolutePath, inputValue))
                        {
                            getData()
                        }
                    }
                })
            }
        }))

        DialogUtil.showOperationListDialog(activity as Context, operationList)
    }

    inner class DataRestoreCallback : BackupUtil.DataRestoreCallback
    {
        override fun onDataRestoreSuccess()
        {
            ToastUtil.showToast(R.string.notify_success_to_restore)
            backToPreFragment()
        }

        override fun onDataRestoreFailed(failedFilePath: String, clearAllData: Boolean, reason: String)
        {
            if (reason == Constant.DATA_RESTORE_FAILED_WRONG_PASSWORD)
            {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.input_encrypt_password), getString(R.string.hint_input_encrypt_password), object : DialogInput.InputCompletedCallback
                {
                    override fun onInputCompleted(inputValue: String)
                    {
                        BackupUtil.restoreData(failedFilePath, inputValue, clearAllData, DataRestoreCallback())
                    }
                })
            }
        }
    }

    override fun onBackPressed(): Boolean
    {
        return false
    }
}