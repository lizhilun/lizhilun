package com.lizl.demo.passwordbox.mvp.fragment

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import com.lizl.demo.passwordbox.R
import com.lizl.demo.passwordbox.adapter.BackupFileListAdapter
import com.lizl.demo.passwordbox.model.OperationItem
import com.lizl.demo.passwordbox.mvp.contract.BackupFileListContract
import com.lizl.demo.passwordbox.mvp.presenter.BackupFileListPresenter
import com.lizl.demo.passwordbox.util.*
import kotlinx.android.synthetic.main.fragment_backup_file_list.*
import java.io.File

class BackupFileListFragment : BaseFragment<BackupFileListPresenter>(), BackupFileListContract.View
{

    private lateinit var backupFileListAdapter: BackupFileListAdapter

    override fun getLayoutResId() = R.layout.fragment_backup_file_list

    override fun initPresenter() = BackupFileListPresenter(this)

    override fun initView()
    {
        ctb_title.setOnBackBtnClickListener { backToPreFragment() }

        backupFileListAdapter = BackupFileListAdapter()
        rv_file_list.layoutManager = LinearLayoutManager(activity)
        rv_file_list.adapter = backupFileListAdapter

        backupFileListAdapter.setOnItemClickListener { onBackupFileItemClick(it) }

        presenter.getAllBackupFiles()
    }

    override fun showBackupFiles(fileList: List<File>)
    {
        backupFileListAdapter.setData(fileList)
    }

    private fun onBackupFileItemClick(file: File)
    {
        val operationList = mutableListOf<OperationItem>().apply {

            // 覆盖导入备份
            add(OperationItem(getString(R.string.import_backup_file_overlay)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_overlay),
                        getString(R.string.notify_restore_data_overlay)) { BackupUtil.restoreData(file.absolutePath, true, DataRestoreCallback()) }
            })

            // 合并导入备份
            add(OperationItem(getString(R.string.import_backup_file_merge)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.import_backup_file_merge),
                        getString(R.string.notify_restore_data_merge)) { BackupUtil.restoreData(file.absolutePath, false, DataRestoreCallback()) }
            })

            // 删除备份文件
            add(OperationItem(getString(R.string.delete_backup_file)) {
                DialogUtil.showOperationConfirmDialog(activity as Context, getString(R.string.delete_backup_file),
                        getString(R.string.notify_delete_backup_file)) {
                    if (FileUtil.deleteFile(file.absolutePath))
                    {
                        presenter.getAllBackupFiles()
                    }
                }
            })

            // 重命名备份文件
            add(OperationItem(getString(R.string.rename_backup_file)) {
                DialogUtil.showInputDialog(activity as Context, getString(R.string.rename_backup_file), getString(R.string.hint_rename_backup_file)) {
                    if (FileUtil.renameFile(file.absolutePath, it))
                    {
                        presenter.getAllBackupFiles()
                    }
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

    override fun onBackPressed() = false
}