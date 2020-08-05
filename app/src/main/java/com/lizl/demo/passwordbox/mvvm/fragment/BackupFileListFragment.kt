package com.lizl.demo.passwordbox.mvvm.fragment

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.BackupFileListAdapter
import com.lizl.demo.passwordbox.model.OperationModel
import com.lizl.demo.passwordbox.mvvm.base.BaseFragment
import com.lizl.demo.passwordbox.mvvm.viewmodel.BackupFileViewModel
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import java.io.File

class BackupFileListFragment : BaseFragment(R.layout.fragment_backup_file_list)
{
    private lateinit var backupFileListAdapter: BackupFileListAdapter

    private lateinit var backupFileViewModel: BackupFileViewModel

    override fun initView()
    {
        backupFileListAdapter = BackupFileListAdapter()
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter

        backupFileViewModel = AndroidViewModelFactory.getInstance(requireActivity().application).create(BackupFileViewModel::class.java)
    }

    override fun initData()
    {
        backupFileViewModel.backupFileLiveData.observe(this, Observer {
            backupFileListAdapter.setData(it)
        })

        backupFileViewModel.requestBackupFileList()
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }
        backupFileListAdapter.setOnItemClickListener { onBackupFileItemClick(it) }
    }

    private fun onBackupFileItemClick(file: File)
    {
        val operationList = mutableListOf<OperationModel>().apply {

            // 覆盖导入备份
            add(OperationModel(getString(R.string.import_backup_file_overlay)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_overlay),
                        getString(R.string.notify_restore_data_overlay)) { BackupUtil.restoreData(file.absolutePath, true, DataRestoreCallback()) }
            })

            // 合并导入备份
            add(OperationModel(getString(R.string.import_backup_file_merge)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_merge),
                        getString(R.string.notify_restore_data_merge)) { BackupUtil.restoreData(file.absolutePath, false, DataRestoreCallback()) }
            })

            // 删除备份文件
            add(OperationModel(getString(R.string.delete_backup_file)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.delete_backup_file),
                        getString(R.string.notify_delete_backup_file)) {
                    if (FileUtil.deleteFile(file.absolutePath))
                    {
                        backupFileViewModel.requestBackupFileList()
                    }
                }
            })

            // 重命名备份文件
            add(OperationModel(getString(R.string.rename_backup_file)) {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.rename_backup_file), getString(R.string.hint_rename_backup_file)) {
                    if (FileUtil.renameFile(file.absolutePath, it))
                    {
                        backupFileViewModel.requestBackupFileList()
                    }
                }
            })

            // 分享备份文件
            add(OperationModel(getString(R.string.share_backup_file)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.share_backup_file),
                        getString(R.string.notify_share_backup_file)) {
                    FileUtil.shareAllTypeFile(file)
                }
            })
        }

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
                DialogUtil.showInputDialog(activity as Context, getString(R.string.input_encrypt_password), getString(R.string.hint_input_encrypt_password)) {
                    BackupUtil.restoreData(failedFilePath, it, clearAllData, DataRestoreCallback())
                }
            }
        }
    }
}