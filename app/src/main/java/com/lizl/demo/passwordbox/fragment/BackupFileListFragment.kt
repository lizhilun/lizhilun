package com.lizl.demo.passwordbox.fragment

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.activity.MainActivity
import com.lizl.demo.passwordbox.adapter.BackupFileListAdapter
import com.lizl.demo.passwordbox.customview.dialog.DialogInput
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationConfirm
import com.lizl.demo.passwordbox.customview.dialog.DialogOperationList
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.util.BackupUtil
import com.lizl.demo.passwordbox.util.Constant
import com.lizl.demo.passwordbox.util.FileUtil
import com.lizl.demo.passwordbox.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BackupFileListFragment : BaseFragment(), BackupFileListAdapter.OnBackFileItemClickListener
{
    private var dialogOperationList: DialogOperationList? = null
    private var dialogOperationConfirm: DialogOperationConfirm? = null
    private var dialogInput: DialogInput? = null

    override fun getLayoutResId(): Int
    {
        return R.layout.fragment_backup_file_list
    }

    override fun initView()
    {
        (activity as MainActivity).setSupportActionBar(toolbar)

        iv_back.setOnClickListener { onBackButtonClick() }
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
        val backupFileListAdapter = BackupFileListAdapter(activity as Context, backupFileList, this)
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter
    }

    override fun onBackupFileItemClick(filePath: String)
    {
        val operationList = mutableListOf<OperationItem>()

        // 覆盖导入备份
        operationList.add(OperationItem(getString(R.string.import_backup_file_overlay), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.import_backup_file_overlay), getString(R.string.notify_restore_data_overlay), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        BackupUtil.restoreData(filePath, true, DataRestoreCallback())
                    }
                })
                dialogOperationConfirm?.show()
            }
        }))

        // 合并导入备份
        operationList.add(OperationItem(getString(R.string.import_backup_file_merge), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                dialogOperationList?.dismiss()

                dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.import_backup_file_merge), getString(R.string.notify_restore_data_merge), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        BackupUtil.restoreData(filePath, false, DataRestoreCallback())
                    }
                })
                dialogOperationConfirm?.show()
            }
        }))

        // 删除备份文件
        operationList.add(OperationItem(getString(R.string.delete_backup_file), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                dialogOperationConfirm = DialogOperationConfirm(activity as Context, getString(R.string.delete_backup_file), getString(R.string.notify_delete_backup_file), object : DialogOperationConfirm.OperationConfirmCallback
                {
                    override fun onOperationConfirmed()
                    {
                        if (FileUtil.deleteFile(filePath))
                        {
                            getData()
                        }
                    }
                })
                dialogOperationConfirm?.show()
            }
        }))

        // 重命名备份文件
        operationList.add(OperationItem(getString(R.string.rename_backup_file), object : OperationItem.OperationItemCallBack
        {
            override fun onOperationExecute()
            {
                dialogOperationList?.dismiss()

                dialogInput = DialogInput(activity as Context, getString(R.string.rename_backup_file), getString(R.string.hint_rename_backup_file), object : DialogInput.InputCompletedCallback
                {
                    override fun onInputCompleted(inputValue: String)
                    {
                        if (FileUtil.renameFile(filePath, inputValue))
                        {
                            getData()
                        }
                    }
                })
                dialogInput?.show()
            }
        }))

        dialogOperationList = DialogOperationList(activity as Context, operationList)
        dialogOperationList?.show()
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
                dialogInput = DialogInput(activity as Context, getString(R.string.input_encrypt_password), getString(R.string.hint_input_encrypt_password), object : DialogInput.InputCompletedCallback
                {
                    override fun onInputCompleted(inputValue: String)
                    {
                        BackupUtil.restoreData(failedFilePath, inputValue, clearAllData, DataRestoreCallback())
                    }
                })
                dialogInput?.show()
            }
        }
    }

    override fun onPause()
    {
        super.onPause()

        dialogOperationConfirm?.dismiss()
        dialogOperationList?.dismiss()
    }

    private fun onBackButtonClick()
    {
        backToPreFragment()
    }

    override fun onBackPressed(): Boolean
    {
        backToPreFragment()
        return true
    }
}